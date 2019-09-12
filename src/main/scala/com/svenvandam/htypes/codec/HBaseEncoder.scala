package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Row

trait HBaseEncoder[T] {
  def encode(t: T): Row
}
