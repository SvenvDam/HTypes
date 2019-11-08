package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.{RowDecoder, RowEncoder}
import org.apache.hadoop.hbase.client.{Delete, Get, Put, Scan}

trait QuerySyntax {
  implicit class GetOps(get: Get) {
    def from[T](implicit decoder: RowDecoder[T]): Get = GetUtils.from[T](get)
  }

  implicit class ScanOps(scan: Scan) {
    def from[T](implicit decoder: RowDecoder[T]): Scan = ScanUtils.from[T](scan)
  }

  implicit class PutOps(put: Put) {
    def from[T](t: T)(implicit encoder: RowEncoder[T]): Put = PutUtils.from(put)(t)
  }

  implicit class DeleteOps(delete: Delete) {
    def from[T](t: T)(implicit encoder: RowEncoder[T]): Delete = DeleteUtils.from(delete)(t)
  }
}

object QuerySyntax extends QuerySyntax
