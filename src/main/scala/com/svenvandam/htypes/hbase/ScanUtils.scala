package com.svenvandam.htypes.hbase

import org.apache.hadoop.hbase.client.Scan

object ScanUtils {

  /**
    * Bind all columns associated with an A to a Scan query
    * @param scan Scan query to bind columns to
    * @param decoder RowDecoder instance for A to retrieve all associated columns
    * @tparam A the type for which we want to get columns
    * @return Scan query with columns bound to it
    */
  def from[A](scan: Scan)(implicit decoder: RowDecoder[A]): Scan =
    decoder
      .getColumns
      .foldLeft(scan) {
        case (s, col) => s.addColumn(col.family, col.qualifier)
      }
}
