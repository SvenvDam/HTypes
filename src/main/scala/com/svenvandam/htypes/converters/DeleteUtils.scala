package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Delete

object DeleteUtils {
  def from[T](delete: Delete)(t: T)(implicit encoder: HBaseEncoder[T]): Delete =
    encoder
      .encode(t)
      .values
      .foldLeft(delete) {
        case (d, (col, CellValue(_, Some(timestamp)))) =>
          d.addColumn(col.getFamilyB, col.getQualifierB, timestamp)
        case (d, (col, CellValue(_, None))) =>
          d.addColumn(col.getFamilyB, col.getQualifierB)
      }

  def createFrom[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete = {
    val row = encoder.encode(t)
    from(new Delete(row.getKeyB))(t)
  }
}
