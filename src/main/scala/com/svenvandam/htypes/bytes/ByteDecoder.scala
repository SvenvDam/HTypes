package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Decoder
import scala.util.control.NonFatal

object ByteDecoder {
  def safeDecode[A](f: Array[Byte] => A)(a: Array[Byte]) = try {
    Some(f(a))
  } catch {
    case NonFatal(_) => None
    case e           => throw e
  }

  def apply[A](f: Array[Byte] => A) = Decoder(safeDecode(f))
}
