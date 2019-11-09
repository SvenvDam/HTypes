package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Encoder

trait ByteEncoder[A] extends Encoder[Array[Byte], A] { self =>
  def contramap[B](f: B => A): ByteEncoder[B] = ByteEncoder(
    (b: B) => self.encode(f(b))
  )
}

object ByteEncoder {
  def apply[A](f: A => Array[Byte]) = new ByteEncoder[A] {
    def encode(a: A): Array[Byte] = f(a)
  }
}
