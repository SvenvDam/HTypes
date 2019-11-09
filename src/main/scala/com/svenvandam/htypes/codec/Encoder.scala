package com.svenvandam.htypes.codec

/**
  * A type class that provides a way to produce an encoded value of type `A` from a `B` value.
  */
private[htypes] trait Encoder[A, B] { self =>

  /**
    * Produce an `A` from a `B`.
    */
  def encode(b: B): A
}

object Encoder {

  /**
    * Produce an `A` from a `B`.
    */
  def encode[A, B](b: B)(implicit encoder: Encoder[A, B]): A =
    encoder.encode(b)
}
