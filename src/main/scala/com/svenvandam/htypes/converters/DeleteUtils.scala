package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Delete

object DeleteUtils {

  /**
    * Bind all information of an object to a Delete query.
    * Deletes a single version.
    * If timestamp is provided in the CellValue, deletion happens at this version.
    * If no timestamp is provided, most recent version is deleted.
    * @param delete Delete query to bind columns to
    * @param t Object to be deleted
    * @param encoder Encoder to encode t to a Row
    * @tparam T Type to be encoded to Row
    * @return Delete query with all columns bound to it
    */
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

  /**
    * Creates a new Delete query and binds all information of an object to it.
    * @param t  Object to be deleted
    * @param encoder Encoded to encode t to a Row
    * @tparam T Type to be encoded to Row
    * @return new Delete query with all columns bound to it
    */
  def createFrom[T](t: T)(implicit encoder: RowEncoder[T]): Delete = {
    val row = encoder.encode(t)
    from(new Delete(row.key))(t)
  }
}
