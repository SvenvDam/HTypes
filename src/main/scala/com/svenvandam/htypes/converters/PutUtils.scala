package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Put

object PutUtils {
  def from[T](put: Put)(t: T)(implicit encoder: RowEncoder[T]): Put =
    encoder
      .encode(t)
      .values
      .foldLeft(put) {
        case (p, (col, CellValue(value, Some(timestamp)))) =>
          p.addColumn(col.family, col.qualifier, timestamp, value)
        case (p, (col, CellValue(value, None))) =>
          p.addColumn(col.family, col.qualifier, value)
      }

  def createFrom[T](t: T)(implicit encoder: RowEncoder[T]): Put = {
    val row = encoder.encode(t)
    from(new Put(row.key))(t)
  }
}
