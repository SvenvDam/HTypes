package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseClassEncoder
import org.apache.hadoop.hbase.client.Get

object GetUtils {
  def from[T](get: Get)(implicit classEncoder: HBaseClassEncoder[T]): Get =
    classEncoder
      .getColumns
      .foldLeft(get) {
        case (g, col) => g.addColumn(col.getFamilyB, col.getQualifierB)
      }
}
