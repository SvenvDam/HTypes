package com.svenvandam.htypes.bytes

object ByteUtils {

  /**
    * Encode a value of type `A` to a byte array using [[ByteEncoder]]
    */
  def toBytes[A](a: A)(implicit encoder: ByteEncoder[A]): Array[Byte] = encoder.encode(a)

  /**
    * Decode a byte array to a value of type `A` using [[ByteDecoder]]
    */
  def fromBytes[A](bytes: Array[Byte])(implicit decoder: ByteDecoder[A]) = decoder.decode(bytes)
}
