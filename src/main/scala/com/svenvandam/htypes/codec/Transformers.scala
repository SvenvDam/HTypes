package com.svenvandam.htypes.codec

import cats.{Contravariant, Functor, Invariant}
import com.svenvandam.htypes.model.Row

trait Transformers {
  implicit val hBaseDecoderFunctor: Functor[HBaseDecoder] =
    new Functor[HBaseDecoder] {
      def map[A, B](fa: HBaseDecoder[A])(f: A => B): HBaseDecoder[B] =
        (row: Row) => fa.decode(row).map(f)
    }

  implicit val hBaseEncoderContravariant: Contravariant[HBaseEncoder] =
    new Contravariant[HBaseEncoder] {
      def contramap[A, B](fa: HBaseEncoder[A])(f: B => A): HBaseEncoder[B] =
        (b: B) => fa.encode(f(b))
    }

  implicit val hBaseCodecInvariant: Invariant[HBaseCodec] =
    new Invariant[HBaseCodec] {
      override def imap[A, B](fa: HBaseCodec[A])(f: A => B)(g: B => A): HBaseCodec[B] =
        new HBaseCodec[B] {
          def decode(row: Row): Option[B] = hBaseDecoderFunctor.map(fa)(f).decode(row)

          def encode(b: B): Row = hBaseEncoderContravariant.contramap(fa)(g).encode(b)
        }
    }
}

object Transformers extends Transformers
