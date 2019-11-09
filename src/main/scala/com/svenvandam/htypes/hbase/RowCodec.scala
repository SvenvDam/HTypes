package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.{Column, Row}

/**
  * Type class that combines a [[RowEncoder]] and a [[RowDecoder]] for type `A`
  */
trait RowCodec[A] extends RowDecoder[A] with RowEncoder[A] { self =>

  /**
    * Create a new [[RowCodec]] by mapping the [[RowDecoder]] and contramapping the [[RowEncoder]] to type `B`
    */
  def imap[B](f: B => A, g: A => B, newColumns: Set[Column]): RowCodec[B] =
    RowCodec.from(self.contramap(f), self.map(g))

  /**
    * Create a new [[RowCodec]] by flatMapping the [[RowDecoder]] and contramapping the [[RowEncoder]] to type `B`
    */
  def iflatMap[B](f: B => A, g: A => Option[B], newColumns: Set[Column]): RowCodec[B] =
    RowCodec.from(self.contramap(f), self.flatMap(g))

  /**
    * Create a new [[RowCodec]] by combining both the [[RowDecoder]] and [[RowEncoder]] with a type `B` to a type `C`
    */
  def combine[B, C](that: RowCodec[B], f: C => (A, B), g: (A, B) => C): RowCodec[C] =
    RowCodec.from(self.combine(that, f), self.combine(that, g))
}

object RowCodec {

  /**
    * Create an instance by providing the required mapping functions and columns
    */
  def apply[A](f: A => Row, g: Row => Option[A], columns: Set[Column]) = new RowCodec[A] {
    def encode(a: A): Row = f(a)
    def decode(row: Row): Option[A] = g(row)
    def getColumns = columns
  }

  /**
    * Create a new instance by providing a [[RowEncoder]] and [[RowDecoder]]
    */
  def from[A](encoder: RowEncoder[A], decoder: RowDecoder[A]) = RowCodec(
    encoder.encode,
    decoder.decode,
    decoder.getColumns
  )
}
