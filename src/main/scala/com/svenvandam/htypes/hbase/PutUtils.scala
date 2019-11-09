package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Put

object PutUtils {

  /**
    * Bind all information of an `A` to a [[Put]] query
    */
  def addValues[A](put: Put)(a: A)(implicit encoder: RowEncoder[A]): Put =
    encoder
      .encode(a)
      .values
      .foldLeft(put) {
        case (p, (col, CellValue(value, Some(timestamp)))) =>
          p.addColumn(col.family, col.qualifier, timestamp, value)
        case (p, (col, CellValue(value, None))) =>
          p.addColumn(col.family, col.qualifier, value)
      }

  /**
    * Creates new [[Put]] query and binds all info of an `A` to it.
    */
  def createFrom[A](a: A)(implicit encoder: RowEncoder[A]): Put = {
    val row = encoder.encode(a)
    addValues(new Put(row.key))(a)
  }
}
