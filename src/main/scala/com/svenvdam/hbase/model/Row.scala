package com.svenvdam.hbase.model

case class Row(key: String, values: Map[Column, CellValue]) {
  def getKeyB: Array[Byte] = key.getBytes
}
