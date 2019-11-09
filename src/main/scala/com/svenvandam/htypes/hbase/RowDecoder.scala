package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.codec.Decoder
import com.svenvandam.htypes.model.{Column, Row}

trait RowDecoder[A] extends Decoder[Row, A] { self =>
  def getColumns: Set[Column]

  def map[B](f: A => B, newColumns: Set[Column]): RowDecoder[B] = RowDecoder(
    self.decode(_).map(f),
    self.getColumns ++ newColumns
  )

  def flatMap[B](f: A => Option[B], newColumns: Set[Column]): RowDecoder[B] = RowDecoder(
    self.decode(_).flatMap(f),
    self.getColumns ++ newColumns
  )

  def combine[B, C](that: RowDecoder[B], f: (A, B) => C): RowDecoder[C] = RowDecoder.combine(self, that, f)
}

object RowDecoder {
  def apply[A](f: Row => Option[A], columns: Set[Column]) = new RowDecoder[A] {
    def decode(row: Row): Option[A] = f(row)
    def getColumns = columns
  }

  def combine[A, B, C](left: RowDecoder[A], right: RowDecoder[B], f: (A, B) => C): RowDecoder[C] = RowDecoder(
    (row: Row) =>
      for {
        l <- left.decode(row)
        r <- right.decode(row)
      } yield f(l, r),
    left.getColumns ++ right.getColumns
  )
}
