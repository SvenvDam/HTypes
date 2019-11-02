package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.ColumnEncoder
import org.apache.hadoop.hbase.client.Scan

object ScanUtils {

  /**
    * Bind all columns associated with an A to a Scan query
    * @param scan Scan query to bind columns to
    * @param columnEncoder ColumnsEncoder instance for A to retrieve all associated columns
    * @tparam A the type for which we want to get columns
    * @return Scan query with columns bound to it
    */
  def from[A](scan: Scan)(implicit columnEncoder: ColumnEncoder[A]): Scan =
    columnEncoder
      .getColumns
      .foldLeft(scan) {
        case (s, col) => s.addColumn(col.family, col.qualifier)
      }
}
