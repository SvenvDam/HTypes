package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Codec

object ByteCodec {
  def apply[A](f: Array[Byte] => A, g: A => Array[Byte]) = Codec[Array[Byte], A](
    ByteDecoder.safeDecode(f),
    g
  )
}
