package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Row
import com.svenvandam.htypes.codec.Encoder

/**
  * A type class that provides a conversion from a value of type `A` to a [[Row]] value.
  */
trait RowEncoder[A] extends Encoder[Row, A] { self =>

  /**
    * Create a new [[RowEncoder]] for type `B` by applying a function to a value of type `B` before encoding as an `A`.
    */
  def contramap[B](f: B => A): RowEncoder[B] =
    RowEncoder((b: B) => self.encode(f(b)))

  /**
    * Create a new [[RowEncoder]] for type `C` by combining this decoder with a [[RowEncoder]] for type `B`
    * and a function which splits a `C` into an `A` and a `B`.
    *
    * Note: it is assumed that the [[Row]]s produced by encoding the `A` and `B` target the same key!
    */
  def combine[B, C](that: RowEncoder[B], f: C => (A, B)): RowEncoder[C] = RowEncoder.combine(self, that, f)
}

object RowEncoder {

  /**
    * Create a new instance.
    */
  def apply[A](f: A => Row) = new RowEncoder[A] {
    def encode(a: A): Row = f(a)
  }

  /**
    * Create a new [[RowEncoder]] for type `C` by combining two [[RowEncoder]]s for type `A` and `B`
    * and a function which splits a `C` into an `A` and a `B`.
    *
    * Note: it is assumed that the [[Row]]s produced by encoding the `A` and `B` target the same key!
    */
  def combine[A, B, C](left: RowEncoder[A], right: RowEncoder[B], f: C => (A, B)): RowEncoder[C] = {
    def encode(c: C) = {
      val (a, b) = f(c)
      val lRow = left.encode(a)
      val rRow = right.encode(b)

      if (lRow.key.toSeq != rRow.key.toSeq)
        throw new IllegalArgumentException(s"Cannot encode $c since it targets two different rows!")

      Row(lRow.key, lRow.values ++ rRow.values)
    }

    RowEncoder(encode)
  }
}
