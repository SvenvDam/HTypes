package com.svenvandam.htypes.converters

import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.{Scan, Put, Delete, Get}

trait QuerySyntax {
  implicit class GetOps(get: Get) {
    def from[T](implicit columnGetter: HBaseClassEncoder[T]): Get =
      columnGetter
        .getColumns
        .foldLeft(get) {
          case (g, col) => g.addColumn(col.getFamilyB, col.getQualifierB)
        }
  }

  implicit class ScanOps(scan: Scan) {
    def from[T](implicit columnGetter: HBaseClassEncoder[T]): Scan =
      columnGetter
        .getColumns
        .foldLeft(scan) {
          case (s, col) => s.addColumn(col.getFamilyB, col.getQualifierB)
        }
  }

  implicit class PutOps(put: Put) {
    def from[T](t: T)(implicit encoder: HBaseEncoder[T]): Put =
      encoder
        .encode(t)
        .values
        .foldLeft(put) {
          case (p, (col, CellValue(value, Some(timestamp)))) =>
            p.addColumn(col.getFamilyB, col.getQualifierB, timestamp, value)
          case (p, (col, CellValue(value, None))) =>
            p.addColumn(col.getFamilyB, col.getQualifierB, value)
        }
  }

  object PutUtil {
    def createFrom[T](t: T)(implicit encoder: HBaseEncoder[T]): Put = {
      val row = encoder.encode(t)
      new Put(row.getKeyB).from(t)
    }
  }

  implicit class DeleteOps(delete: Delete) {
    def from[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete =
      encoder
        .encode(t)
        .values
        .foldLeft(delete) {
          case (d, (col, CellValue(_, Some(timestamp)))) =>
            d.addColumn(col.getFamilyB, col.getQualifierB, timestamp)
          case (d, (col, CellValue(_, None))) =>
            d.addColumn(col.getFamilyB, col.getQualifierB)
        }
  }

  object DeleteUtil {
    def createFrom[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete = {
      val row = encoder.encode(t)
      new Delete(row.getKeyB).from(t)
    }
  }
}

object QuerySyntax extends QuerySyntax
