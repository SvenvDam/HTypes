package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.codec.Decoder
import com.svenvandam.htypes.model.{Column, Row}

trait RowDecoder[A] extends Decoder[Row, A] {
  def getColumns: Set[Column]
}
