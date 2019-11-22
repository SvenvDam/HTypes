package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.codec.Decoder
import com.svenvandam.htypes.model.{Column, Row}

/**
  * A type class that provides a way to produce a value of type `A` from a [[Row]] value.
  */
trait RowDecoder[A] extends Decoder[Row, A] { self =>
  def getColumns: Set[Column]

  /**
    * Create a new [[RowDecoder]] for type `B` by applying a function to a value of type `A` after decoding.
    */
  def map[B](f: A => B): RowDecoder[B] = RowDecoder(
    self.decode(_).map(f),
    self.getColumns
  )

  /**
    * Create a new [[RowDecoder]] for type `B` by applying a function which might fail to a value of type `A` after decoding.
    */
  def flatMap[B](f: A => Option[B]): RowDecoder[B] = RowDecoder(
    self.decode(_).flatMap(f),
    self.getColumns
  )

  /**
    * Create a new [[RowDecoder]] for type `C` by combining this decoder with a [[RowDecoder]] for type `B`
    * and a function which combines an `A` and a `B` to a C.
    */
  def combine[B, C](that: RowDecoder[B], f: (A, B) => C): RowDecoder[C] = RowDecoder.combine(self, that, f)
}

object RowDecoder {

  /**
    * Create a new instance.
    */
  def apply[A](f: Row => Option[A], columns: Set[Column]) = new RowDecoder[A] {
    def decode(row: Row): Option[A] = f(row)
    def getColumns = columns
  }

  /**
    * Create a new [[RowDecoder]] by merging two instances using a function which combines an `A` and a `B` into a `C`.
    */
  def combine[A, B, C](left: RowDecoder[A], right: RowDecoder[B], f: (A, B) => C): RowDecoder[C] = RowDecoder(
    (row: Row) =>
      for {
        l <- left.decode(row)
        r <- right.decode(row)
      } yield f(l, r),
    left.getColumns ++ right.getColumns
  )
}
