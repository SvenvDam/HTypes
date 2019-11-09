package com.svenvandam.htypes.hbase

import org.apache.hadoop.hbase.client.Scan

object ScanUtils {

  /**
    * Bind all columns associated with an `A` to a [[Scan]].
    */
  def addColumns[A](scan: Scan)(implicit decoder: RowDecoder[A]): Scan =
    decoder
      .getColumns
      .foldLeft(scan) {
        case (s, col) => s.addColumn(col.family, col.qualifier)
      }
}
