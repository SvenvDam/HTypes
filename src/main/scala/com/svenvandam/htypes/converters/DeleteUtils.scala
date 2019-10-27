package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Delete

object DeleteUtils {
  def from[T](delete: Delete)(t: T)(implicit encoder: RowEncoder[T]): Delete =
    encoder
      .encode(t)
      .values
      .foldLeft(delete) {
        case (d, (col, CellValue(_, Some(timestamp)))) =>
          d.addColumn(col.family, col.qualifier, timestamp)
        case (d, (col, CellValue(_, None))) =>
          d.addColumn(col.family, col.qualifier)
      }

  def createFrom[T](t: T)(implicit encoder: RowEncoder[T]): Delete = {
    val row = encoder.encode(t)
    from(new Delete(row.key))(t)
  }
}
