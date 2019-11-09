package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Encoder

/**
  * A type class that provides a conversion from a value of type `A` to a byte array.
  */
trait ByteEncoder[A] extends Encoder[Array[Byte], A] { self =>

  /**
    * Create a new [[ByteEncoder]] for type `B` by applying a function to a value of type `B` before encoding as an `A`.
    */
  def contramap[B](f: B => A): ByteEncoder[B] = ByteEncoder(
    (b: B) => self.encode(f(b))
  )
}

object ByteEncoder {

  /**
    * Create a new instance.
    */
  def apply[A](f: A => Array[Byte]) = new ByteEncoder[A] {
    def encode(a: A): Array[Byte] = f(a)
  }
}
