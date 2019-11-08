package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowDecoder
import org.apache.hadoop.hbase.client.Get

object GetUtils {

  /**
    * Bind all columns associated with type A to a get
    * @param get the Get query to bind columns to
    * @param decoder RowDecoder instance for A to retrieve all associated columns
    * @tparam A the type for which we want to get columns
    * @return Get query with columns bound to it
    */
  def from[A](get: Get)(implicit decoder: RowDecoder[A]): Get =
    decoder
      .getColumns
      .foldLeft(get) {
        case (g, col) => g.addColumn(col.family, col.qualifier)
      }
}
