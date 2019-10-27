package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.ColumnEncoder
import org.apache.hadoop.hbase.client.Scan

object ScanUtils {
  def from[T](scan: Scan)(implicit classEncoder: ColumnEncoder[T]): Scan =
    classEncoder
      .getColumns
      .foldLeft(scan) {
        case (s, col) => s.addColumn(col.getFamilyB, col.getQualifierB)
      }
}
