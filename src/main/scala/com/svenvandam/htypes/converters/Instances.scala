package com.svenvandam.htypes.converters

import cats.{Functor, Contravariant}
import com.svenvandam.htypes.model.Row

// TODO: rename
trait Instances {
  implicit val hBaseDecoderFunctor: Functor[HBaseDecoder] =
    new Functor[HBaseDecoder] {
      override def map[A, B](fa: HBaseDecoder[A])(f: A => B): HBaseDecoder[B] =
        (row: Row) => fa.decode(row).map(f)
    }

  implicit val hBaseEncoderContravariant: Contravariant[HBaseEncoder] =
    new Contravariant[HBaseEncoder] {
      override def contramap[A, B](fa: HBaseEncoder[A])(f: B => A): HBaseEncoder[B] =
        (t: B) => fa.encode(f(t))
    }
}

object Instances extends Instances
