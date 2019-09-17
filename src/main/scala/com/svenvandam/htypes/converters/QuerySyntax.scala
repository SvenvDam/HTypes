package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.{HBaseClassEncoder, HBaseEncoder}
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.{Scan, Put, Delete, Get}

trait QuerySyntax {
  implicit class GetOps(get: Get) {
    def from[T](implicit classEncoder: HBaseClassEncoder[T]): Get = GetUtils.from[T](get)
  }

  implicit class ScanOps(scan: Scan) {
    def from[T](implicit classEncoder: HBaseClassEncoder[T]): Scan = ScanUtils.from[T](scan)
  }

  implicit class PutOps(put: Put) {
    def from[T](t: T)(implicit encoder: HBaseEncoder[T]): Put = PutUtils.from(put)(t)
  }

  implicit class DeleteOps(delete: Delete) {
    def from[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete = DeleteUtils.from(delete)(t)
  }
}

object QuerySyntax extends QuerySyntax
