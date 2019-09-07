package com.svenvdam.hbase.model

case class DecodedValue[T](value: T, timestamp: Long)
