package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Row

trait HBaseCodec[A] extends HBaseEncoder[A] with HBaseDecoder[A] { self =>
  def imap[B](f: A => B, g: B => A): HBaseCodec[B] = new HBaseCodec[B] {
    def decode(row: Row): Option[B] = self.map(f).decode(row)
    def encode(b: B): Row = self.contramap(g).encode(b)
  }
}
