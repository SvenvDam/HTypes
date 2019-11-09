package com.svenvandam.htypes.bytes

object ByteUtils {
  def toBytes[A](a: A)(implicit encoder: ByteEncoder[A]): Array[Byte] = encoder.encode(a)
  def fromBytes[A](bytes: Array[Byte])(implicit decoder: ByteDecoder[A]) = decoder.decode(bytes)
}
