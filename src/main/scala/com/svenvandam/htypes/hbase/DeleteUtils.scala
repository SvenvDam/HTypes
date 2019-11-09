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
  def addColumnsSingleVersion[A](delete: Delete)(a: A)(implicit encoder: RowEncoder[A]): Delete =
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
    * Bind all information of an object to a [[Delete]] query.
    * Deletes all versions (up to timestamp if specified).
    *
    * If the timestamp is being set through the .setTimestamp() methods of [[Delete]], call this before this method or it wont work!
    */
  def addColumnsAllVersions[A](delete: Delete)(a: A)(implicit encoder: RowEncoder[A]): Delete =
    encoder
      .encode(a)
      .values
      .foldLeft(delete) {
        case (d, (col, CellValue(_, Some(timestamp)))) =>
          d.addColumns(col.family, col.qualifier, timestamp)
        case (d, (col, CellValue(_, None))) =>
          d.addColumns(col.family, col.qualifier)
      }

  /**
    * Creates a new [[Delete]] query and binds all information of an object to it.
    * Deletes a single version.
    */
  def createFromSingleVersion[A](a: A)(implicit encoder: RowEncoder[A]): Delete = {
    val row = encoder.encode(a)
    addColumnsSingleVersion(new Delete(row.key))(a)
  }

  /**
    * Creates a new [[Delete]] query and binds all information of an object to it.
    * Deletes a all versions (up to timestamp if specified).
    *
    * If the timestamp is being set through the .setTimestamp() methods of [[Delete]], call this before this method or it wont work!
    */
  def createFromAllVersions[A](a: A)(implicit encoder: RowEncoder[A]): Delete = {
    val row = encoder.encode(a)
    addColumnsAllVersions(new Delete(row.key))(a)
  }
}
