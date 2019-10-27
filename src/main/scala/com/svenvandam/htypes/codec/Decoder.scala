package com.svenvandam.htypes.codec

private[htypes] trait Decoder[A, B] { self =>
  def decode(a: A): Option[B]

  def map[C](f: B => C): Decoder[A, C] = new Decoder[A, C] {
    def decode(a: A): Option[C] = self.decode(a).map(f)
  }
}

object Decoder {
  def apply[A, B](f: A => Option[B]): Decoder[A, B] = new Decoder[A, B] {
    def decode(a: A): Option[B] = f(a)
  }
}
