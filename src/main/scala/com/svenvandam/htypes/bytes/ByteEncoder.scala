package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Encoder

object ByteEncoder {
  def apply[A] = Encoder[Array[Byte], A] _
}
