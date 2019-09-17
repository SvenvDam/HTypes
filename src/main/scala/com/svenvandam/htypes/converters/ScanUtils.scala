package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseClassEncoder
import org.apache.hadoop.hbase.client.Scan

object ScanUtils {
  def from[T](scan: Scan)(implicit classEncoder: HBaseClassEncoder[T]): Scan =
    classEncoder
      .getColumns
      .foldLeft(scan) {
        case (s, col) => s.addColumn(col.getFamilyB, col.getQualifierB)
      }
}
