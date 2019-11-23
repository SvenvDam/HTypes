package com.svenvandam.htypes.bytes

/**
  * Type class that combines a [[ByteEncoder]] and a [[ByteDecoder]] for type `A`
  */
trait ByteCodec[A] extends ByteDecoder[A] with ByteEncoder[A] { self =>

  /**
    * Create a new [[ByteCodec]] by mapping the [[ByteDecoder]] and contramapping the [[ByteEncoder]] to type `B`
    */
  def imap[B](f: B => A, g: A => B): ByteCodec[B] =
    ByteCodec.from(self.contramap(f), self.map(g))

  /**
    * Create a new [[ByteCodec]] by flatMapping the [[ByteDecoder]] and contramapping the [[ByteEncoder]] to type `B`
    */
  def iflatMap[B](f: B => A, g: A => Option[B]): ByteCodec[B] =
    ByteCodec.from(self.contramap(f), self.flatMap(g))
}

object ByteCodec {

  /**
    * Create a new instance using a safe decode function and an encode function.
    */
  def apply[A](f: A => Array[Byte], g: Array[Byte] => Option[A]) = new ByteCodec[A] {
    def encode(a: A): Array[Byte] = f(a)
    def decode(bytes: Array[Byte]): Option[A] = g(bytes)
  }

  /**
    * Create a new instance using a wrapped unsafe decode function and an encode function.
    */
  def safeApply[A](f: A => Array[Byte], g: Array[Byte] => A) = new ByteCodec[A] {
    def encode(a: A): Array[Byte] = f(a)
    def decode(bytes: Array[Byte]): Option[A] = ByteDecoder.safeDecode(g)(bytes)
  }

  /**
    * Create a new instance by providing a [[ByteEncoder]] and [[ByteDecoder]]
    */
  def from[A](encoder: ByteEncoder[A], decoder: ByteDecoder[A]): ByteCodec[A] = ByteCodec(
    encoder.encode,
    decoder.decode
  )
}
