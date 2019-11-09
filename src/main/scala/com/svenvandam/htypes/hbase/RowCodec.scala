package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.{Column, Row}

trait RowCodec[A] extends RowDecoder[A] with RowEncoder[A] { self =>
  def imap[B](f: B => A, g: A => B, newColumns: Set[Column]): RowCodec[B] =
    RowCodec.from(self.contramap(f), self.map(g, newColumns))

  def iflatMap[B](f: B => A, g: A => Option[B], newColumns: Set[Column]): RowCodec[B] =
    RowCodec.from(self.contramap(f), self.flatMap(g, newColumns))

  def combine[B, C](that: RowCodec[B], f: C => (A, B), g: (A, B) => C): RowCodec[C] =
    RowCodec.from(self.combine(that, f), self.combine(that, g))
}

object RowCodec {
  def apply[A](f: A => Row, g: Row => Option[A], columns: Set[Column]) = new RowCodec[A] {
    def encode(a: A): Row = f(a)
    def decode(row: Row): Option[A] = g(row)
    def getColumns = columns
  }

  def from[A](encoder: RowEncoder[A], decoder: RowDecoder[A]) = RowCodec(
    encoder.encode,
    decoder.decode,
    decoder.getColumns
  )
}
