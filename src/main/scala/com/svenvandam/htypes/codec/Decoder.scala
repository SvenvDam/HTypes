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

  /**
    * Compose a Decoder from the same encoded form to a new type
    * @param f mapping function transforming a B which can be decoded to a C
    * @tparam C new decoded form
    * @return Decoder from A to C
    */
  def map[C](f: B => C): Decoder[A, C] = new Decoder[A, C] {
    def decode(a: A): Option[C] = self.decode(a).map(f)
  }
}

object Decoder {
  def apply[A, B](f: A => Option[B]): Decoder[A, B] = new Decoder[A, B] {
    def decode(a: A): Option[B] = f(a)
  }

  def decode[A, B](a: A)(implicit decoder: Decoder[A, B]): Option[B] =
    decoder.decode(a)
}
