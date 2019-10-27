package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Decoder

trait ByteSyntax {
  implicit class ByteOps(bytes: Array[Byte]) {
    def as[A](implicit decoder: Decoder[Array[Byte], A]): Option[A] = decoder.decode(bytes)
  }
}

object ByteSyntax extends ByteSyntax
