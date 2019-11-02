package com.svenvandam.htypes.codec

/**
  * Typeclass describing encoding behaviour.
  * @tparam A encoded form, resulting type after encoding
  * @tparam B decoded form, type to be encoded
  */
private[htypes] trait Encoder[A, B] { self =>

  def encode(b: B): A

  /**
    * Compose an Encoder to the same target for a new type
    * @param f mapping function transforming a C to a B which can be encoded
    * @tparam C new decoded form
    * @return Encoder from C to A
    */
  def contramap[C](f: C => B): Encoder[A, C] = new Encoder[A, C] {
    def encode(c: C): A = self.encode(f(c))
  }
}

object Encoder {
  def apply[A, B](f: B => A): Encoder[A, B] = new Encoder[A, B] {
    def encode(b: B): A = f(b)
  }

  def encode[A, B](b: B)(implicit encoder: Encoder[A, B]): A =
    encoder.encode(b)
}
