package com.svenvandam.htypes.hbase.query

import com.svenvandam.htypes.hbase.{RowDecoder, RowEncoder}
import org.apache.hadoop.hbase.client.{Delete, Get, Put, Scan}

trait QuerySyntax {
  implicit class GetOps(get: Get) {

    /**
      * Bind all columns associated with type `A` to this [[Get]].
      */
    def addColumnsFrom[T](implicit decoder: RowDecoder[T]): Get = GetUtils.addColumns[T](get)
  }

  implicit class ScanOps(scan: Scan) {

    /**
      * Bind all columns associated with type `A` to this [[Scan]].
      */
    def addColumnsFrom[T](implicit decoder: RowDecoder[T]): Scan = ScanUtils.addColumns[T](scan)
  }

  implicit class PutOps(put: Put) {

    /**
      * Bind all information of an `A` to this [[Put]].
      */
    def addValuesFrom[T](t: T)(implicit encoder: RowEncoder[T]): Put = PutUtils.addValues(put)(t)
  }

  implicit class DeleteOps(delete: Delete) {

    /**
      * Bind all columns associated with type `A` to this [[Delete]].
      * Deletes single version.
      */
    def addColumnsFromSingleVersion[T](t: T)(implicit encoder: RowEncoder[T]): Delete =
      DeleteUtils.addColumnsSingleVersion(delete)(t)

    /**
      * Bind all columns associated with type `A` to this [[Delete]].
      * Deletes all versions.
      */
    def addColumnsFromAllVersions[T](t: T)(implicit encoder: RowEncoder[T]): Delete =
      DeleteUtils.addColumnsAllVersions(delete)(t)
  }
}

object QuerySyntax extends QuerySyntax
