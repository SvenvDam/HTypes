package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Delete

object DeleteUtils {

  /**
    * Bind all information of an object to a [[Delete]] query.
    * Deletes a single version.
    * If timestamp is provided in the [[CellValue]], deletion happens at this version.
    * If no timestamp is provided, most recent version is deleted.
    */
  def addColumns[A](delete: Delete)(a: A)(implicit encoder: RowEncoder[A]): Delete =
    encoder
      .encode(a)
      .values
      .foldLeft(delete) {
        case (d, (col, CellValue(_, Some(timestamp)))) =>
          d.addColumn(col.family, col.qualifier, timestamp)
        case (d, (col, CellValue(_, None))) =>
          d.addColumn(col.family, col.qualifier)
      }

  /**
    * Creates a new [[Delete]] query and binds all information of an object to it.
    */
  def createFrom[A](a: A)(implicit encoder: RowEncoder[A]): Delete = {
    val row = encoder.encode(a)
    addColumns(new Delete(row.key))(a)
  }
}
