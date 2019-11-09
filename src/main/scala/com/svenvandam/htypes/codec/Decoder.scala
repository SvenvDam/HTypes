package com.svenvandam.htypes.codec

/**
  * Typeclass describing decoding behaviour
  * @tparam A encoded form, type to be decoded
  * @tparam B decoded form, resulting type after decoding
  */
private[htypes] trait Decoder[A, B] { self =>

  /**
    * Performs decoding step which might possibly fail
    * @param a encoded object
    * @return Some(decoded object) if decoding is successful, else None
    */
  def decode(a: A): Option[B]
}

object Decoder {
  def decode[A, B](a: A)(implicit decoder: Decoder[A, B]): Option[B] =
    decoder.decode(a)
}
