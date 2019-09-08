package com.svenvandam.htypes.converters

import com.svenvandam.htypes.model.Row

trait HBaseEncoder[T] {
  def encode(t: T): Row
}
