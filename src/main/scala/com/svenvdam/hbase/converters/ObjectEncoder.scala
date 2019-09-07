package com.svenvdam.hbase.converters

import com.svenvdam.hbase.model.CellValue
import org.apache.hadoop.hbase.client.{Put, Delete}

object ObjectEncoder {
  implicit def toPut[T](t: T)(implicit encoder: HBaseEncoder[T]): Put = {
    val row = encoder.encode(t)
    row.values
      .foldLeft(new Put(row.getKeyB)) {
        case (put, (col, CellValue(v, Some(time)))) =>
          put.addColumn(col.getFamilyB, col.getQualifierB, time, v)

        case (put, (col, CellValue(v, None))) =>
          put.addColumn(col.getFamilyB, col.getQualifierB, v)
      }
  }

  implicit def toDelete[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete = {
    val row = encoder.encode(t)
    row.values.foldLeft(new Delete(row.getKeyB)) {
      case (del, (col, _)) => del.addColumn(col.getFamilyB, col.getQualifierB)
    }
  }
}
