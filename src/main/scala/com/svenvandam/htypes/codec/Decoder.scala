package com.svenvandam.htypes.codec

/**
  * A type class that provides a way to produce a decoded value of type `B` from an `A` value.
  */
private[htypes] trait Decoder[A, B] { self =>

  /**
    * Produce a `B` from an `A` which could fail.
    */
  def decode(a: A): Option[B]
}

object Decoder {

  /**
    * Produce a `B` from an `A` which could fail.
    */
  def decode[A, B](a: A)(implicit decoder: Decoder[A, B]): Option[B] =
    decoder.decode(a)
}
