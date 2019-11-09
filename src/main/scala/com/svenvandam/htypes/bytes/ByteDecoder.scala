package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Decoder
import scala.util.control.NonFatal

/**
  * A type class that provides a way to produce a value of type `A` from a byte array.
  */
trait ByteDecoder[A] extends Decoder[Array[Byte], A] { self =>

  /**
    * Create a new [[ByteDecoder]] for type `B` by applying a function to a value of type `A` after decoding.
    */
  def map[B](f: A => B): ByteDecoder[B] = ByteDecoder(
    self.decode(_).map(f)
  )

  /**
    * Create a new [[ByteDecoder]] for type `B` by applying a function which might fail to a value of type `A` after decoding.
    */
  def flatMap[B](f: A => Option[B]): ByteDecoder[B] = ByteDecoder(
    self.decode(_).flatMap(f)
  )
}

object ByteDecoder {

  /**
    * Perform a possibly unsafe decoding and wrap nonfatal errors to the failure case.
    */
  def safeDecode[A](f: Array[Byte] => A)(a: Array[Byte]) = try {
    Some(f(a))
  } catch {
    case NonFatal(_)  => None
    case e: Throwable => throw e
  }

  /**
    * Create anew instance by supplying a safe decoding function
    */
  def apply[A](f: Array[Byte] => Option[A]) = new ByteDecoder[A] {
    def decode(bytes: Array[Byte]): Option[A] = f(bytes)
  }

  /**
    * Create anew instance by supplying an unsafe decoding function and wrapping it using safeDecode.
    */
  def safeApply[A](f: Array[Byte] => A) = new ByteDecoder[A] {
    def decode(bytes: Array[Byte]): Option[A] = safeDecode[A](f)(bytes)
  }
}
