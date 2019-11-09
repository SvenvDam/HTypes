package com.svenvandam.htypes.bytes

trait ByteCodec[A] extends ByteDecoder[A] with ByteEncoder[A] { self =>
  def imap[B](f: B => A, g: A => B): ByteCodec[B] =
    ByteCodec.from(self.contramap(f), self.map(g))

  def iflatMap[B](f: B => A, g: A => Option[B]): ByteCodec[B] =
    ByteCodec.from(self.contramap(f), self.flatMap(g))
}

object ByteCodec {
  def apply[A](f: A => Array[Byte], g: Array[Byte] => Option[A]) = new ByteCodec[A] {
    def encode(a: A): Array[Byte] = f(a)
    def decode(bytes: Array[Byte]): Option[A] = g(bytes)
  }

  def safeApply[A](f: A => Array[Byte], g: Array[Byte] => A) = new ByteCodec[A] {
    def encode(a: A): Array[Byte] = f(a)
    def decode(bytes: Array[Byte]): Option[A] = ByteDecoder.safeDecode(g)(bytes)
  }

  def from[A](encoder: ByteEncoder[A], decoder: ByteDecoder[A]): ByteCodec[A] = ByteCodec(
    encoder.encode,
    decoder.decode
  )
}
