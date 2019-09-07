package com.svenvdam.hbase.converters

import com.svenvdam.hbase.model.Row

trait HBaseDecoder[T] {
  def decode(row: Row): Option[T]
}
