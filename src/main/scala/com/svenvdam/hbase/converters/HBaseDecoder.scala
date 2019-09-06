package com.svenvdam.hbase.converters

import org.apache.hadoop.hbase.client.Result

trait HBaseDecoder[T] {
  def decode(result: Result): T
}
