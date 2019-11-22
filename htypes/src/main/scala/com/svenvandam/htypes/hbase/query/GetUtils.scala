package com.svenvandam.htypes.hbase.query

import com.svenvandam.htypes.hbase.RowDecoder
import org.apache.hadoop.hbase.client.Get

object GetUtils {

  /**
    * Bind all columns associated with type `A` to a [[Get]]
    */
  def addColumns[A](get: Get)(implicit decoder: RowDecoder[A]): Get =
    decoder
      .getColumns
      .foldLeft(get) {
        case (g, col) => g.addColumn(col.family, col.qualifier)
      }
}
