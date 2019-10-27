package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.ColumnEncoder
import org.apache.hadoop.hbase.client.Get

object GetUtils {
  def from[T](get: Get)(implicit classEncoder: ColumnEncoder[T]): Get =
    classEncoder
      .getColumns
      .foldLeft(get) {
        case (g, col) => g.addColumn(col.getFamilyB, col.getQualifierB)
      }
}
