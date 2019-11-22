package com.svenvandam.htypes.bytes

trait ByteSyntax {
  implicit class ByteOps(bytes: Array[Byte]) {

    /**
      * Decode this byte array to a value of type `A` using [[ByteDecoder]]
      */
    def as[A](implicit decoder: ByteDecoder[A]): Option[A] = ByteUtils.fromBytes(bytes)
  }
}

object ByteSyntax extends ByteSyntax
