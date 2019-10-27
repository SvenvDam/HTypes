package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Encoder

trait ByteEncoder[A] extends Encoder[Array[Byte], A]

object ByteEncoder {
  def apply[A] = Encoder[Array[Byte], A]
}
