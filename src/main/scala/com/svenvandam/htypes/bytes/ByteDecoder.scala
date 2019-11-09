package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Decoder
import scala.util.control.NonFatal

trait ByteDecoder[A] extends Decoder[Array[Byte], A] { self =>
  def map[B](f: A => B): ByteDecoder[B] = ByteDecoder(
    self.decode(_).map(f)
  )

  def flatMap[B](f: A => Option[B]): ByteDecoder[B] = ByteDecoder(
    self.decode(_).flatMap(f)
  )
}

object ByteDecoder {
  def safeDecode[A](f: Array[Byte] => A)(a: Array[Byte]) = try {
    Some(f(a))
  } catch {
    case NonFatal(_)  => None
    case e: Throwable => throw e
  }

  def apply[A](f: Array[Byte] => Option[A]) = new ByteDecoder[A] {
    def decode(bytes: Array[Byte]): Option[A] = f(bytes)
  }

  def safeApply[A](f: Array[Byte] => A) = new ByteDecoder[A] {
    def decode(bytes: Array[Byte]): Option[A] = safeDecode[A](f)(bytes)
  }
}
