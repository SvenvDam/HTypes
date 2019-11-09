package com.svenvandam.htypes.codec

/**
  * Typeclass describing encoding behaviour.
  *
  * @tparam A encoded form, resulting type after encoding
  * @tparam B decoded form, type to be encoded
  */
private[htypes] trait Encoder[A, B] { self =>

  def encode(b: B): A
}

object Encoder {
  def encode[A, B](b: B)(implicit encoder: Encoder[A, B]): A =
    encoder.encode(b)
}
