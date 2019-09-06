package com.svenvdam.hbase.model

private[hbase] case class Row(key: String, values: Map[Column, Value]) {
  def getKeyB: Array[Byte] = key.getBytes
}
