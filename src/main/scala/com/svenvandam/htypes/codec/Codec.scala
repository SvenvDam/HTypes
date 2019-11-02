package com.svenvandam.htypes.codec

/**
  * Typeclass describing bidirectional transformation between decoded and encoded form.
  * Combines Decoder and Encoder.
  * @tparam A encoded form
  * @tparam B decoded form
  */
private[htypes] trait Codec[A, B] extends Decoder[A, B] with Encoder[A, B] { self =>

  /**
    * Compose a Codec for a new decoded form
    * @param f mapping to new decoded form
    * @param g mapping from new decoded form
    * @tparam C new decoded form
    * @return Codec between A and C
    */
  def imap[C](f: B => C, g: C => B): Codec[A, C] = new Codec[A, C] {
    def decode(a: A): Option[C] = self.map(f).decode(a)
    def encode(c: C): A = self.contramap(g).encode(c)
  }
}

object Codec {
  def apply[A, B](f: A => Option[B], g: B => A): Codec[A, B] = new Codec[A, B] {
    def decode(a: A): Option[B] = f(a)
    def encode(b: B): A = g(b)
  }

  def decode[A, B](a: A)(implicit decoder: Decoder[A, B]): Option[B] = Decoder.decode(a)
  def encode[A, B](b: B)(implicit encoder: Encoder[A, B]): A = Encoder.encode(b)
}
