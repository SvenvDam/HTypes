package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Row

trait HBaseDecoder[T] {
  def decode(row: Row): Option[T]
}
