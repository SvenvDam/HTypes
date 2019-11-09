package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Row
import com.svenvandam.htypes.codec.Encoder

trait RowEncoder[A] extends Encoder[Row, A] { self =>
  def contramap[B](f: B => A): RowEncoder[B] =
    RowEncoder((b: B) => self.encode(f(b)))

  def combine[B, C](that: RowEncoder[B], f: C => (A, B)): RowEncoder[C] = RowEncoder.combine(self, that, f)
}

object RowEncoder {
  def apply[A](f: A => Row) = new RowEncoder[A] {
    def encode(a: A): Row = f(a)
  }

  def combine[A, B, C](left: RowEncoder[A], right: RowEncoder[B], f: C => (A, B)): RowEncoder[C] = {
    def encode(c: C) = {
      val (a, b) = f(c)
      val lRow = left.encode(a)
      val rRow = right.encode(b)

      if (lRow.key.toSeq != rRow.key.toSeq)
        throw new IllegalArgumentException(s"Cannot encode $c since it targets two different rows!")

      Row(lRow.key, lRow.values ++ rRow.values)
    }

    RowEncoder(encode)
  }
}
