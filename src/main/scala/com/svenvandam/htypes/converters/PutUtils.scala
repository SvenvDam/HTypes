package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Put

object PutUtils {
  def from[T](put: Put)(t: T)(implicit encoder: HBaseEncoder[T]): Put =
    encoder
      .encode(t)
      .values
      .foldLeft(put) {
        case (p, (col, CellValue(value, Some(timestamp)))) =>
          p.addColumn(col.getFamilyB, col.getQualifierB, timestamp, value)
        case (p, (col, CellValue(value, None))) =>
          p.addColumn(col.getFamilyB, col.getQualifierB, value)
      }

  def createFrom[T](t: T)(implicit encoder: HBaseEncoder[T]): Put = {
    val row = encoder.encode(t)
    from(new Put(row.getKeyB))(t)
  }
}
