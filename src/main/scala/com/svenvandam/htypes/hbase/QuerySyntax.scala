package com.svenvandam.htypes.hbase

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

  /**
    * Bind all columns associated with type `A` to this [[Delete]].
    */
  implicit class DeleteOps(delete: Delete) {
    def addColumnsFrom[T](t: T)(implicit encoder: RowEncoder[T]): Delete = DeleteUtils.addColumns(delete)(t)
  }
}

object QuerySyntax extends QuerySyntax
