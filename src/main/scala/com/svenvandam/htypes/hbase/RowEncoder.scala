package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Row
import com.svenvandam.htypes.codec.Encoder

trait RowEncoder[A] extends Encoder[Row, A] { self =>
  def contramap[B](f: B => A): RowEncoder[B] =
    RowEncoder((b: B) => self.encode(f(b)))
}

object RowEncoder {
  def apply[A](f: A => Row) = new RowEncoder[A] {
    def encode(a: A): Row = f(a)
  }
}
