package com.svenvandam.htypes.codec

private[htypes] trait Codec[A, B] extends Decoder[A, B] with Encoder[A, B] { self =>
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
