package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Row

trait HBaseDecoder[A] { self =>
  def decode(row: Row): Option[A]

  def map[B](f: A => B): HBaseDecoder[B] = new HBaseDecoder[B] {
    def decode(row: Row): Option[B] = self.decode(row).map(f)
  }
}
