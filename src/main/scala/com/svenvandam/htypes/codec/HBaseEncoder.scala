package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Row

trait HBaseEncoder[A] { self =>

  def encode(t: A): Row

  def contramap[B](f: B => A): HBaseEncoder[B] = new HBaseEncoder[B] {
    def encode(b: B): Row = self.encode(f(b))
  }
}
