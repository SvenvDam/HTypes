package com.svenvandam.htypes.async

import org.apache.hadoop.hbase.client._

trait TableSyntax {

  implicit class TableOps(table: Table) {

    def getScannerAsync[A[_]](scan: Scan)(implicit backend: AsyncBackend[A]): A[ResultScanner] =
      TableUtils.scanAsync(table)(scan)

    def getAsync[A[_]](get: Get)(implicit backend: AsyncBackend[A]): A[Result] =
      TableUtils.getAsync(table)(get)

    def putAsync[A[_]](put: Put)(implicit backend: AsyncBackend[A]): A[Unit] =
      TableUtils.putAsync(table)(put)

    def deleteAsync[A[_]](delete: Delete)(implicit backend: AsyncBackend[A]): A[Unit] =
      TableUtils.deleteAsync(table)(delete)
  }
}

object TableSyntax extends TableSyntax
