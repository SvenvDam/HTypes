package com.svenvdam.hbase.converters

import com.svenvdam.hbase.model.{Value, Column, Row}

trait HBaseEncoder[T] {
  def encode(t: T): Row
}
