package com.svenvandam.htypes.codec

private[htypes] trait Encoder[A, B] { self =>
  def encode(b: B): A

  def contramap[C](f: C => B): Encoder[A, C] = new Encoder[A, C] {
    def encode(c: C): A = self.encode(f(c))
  }
}

object Encoder {
  def apply[A, B](f: B => A): Encoder[A, B] = new Encoder[A, B] {
    def encode(b: B): A = f(b)
  }
}
