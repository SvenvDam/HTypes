package com.svenvdam.hbase.model

private[hbase] case class Value(value: Array[Byte], timestamp: Option[Long])
