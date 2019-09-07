package com.svenvdam.hbase.model

case class CellValue(value: Array[Byte], timestamp: Option[Long] = None)
