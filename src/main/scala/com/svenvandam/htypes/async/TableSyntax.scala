package com.svenvandam.htypes.async

import org.apache.hadoop.hbase.client._

trait TableSyntax {

  implicit class TableOps(table: Table) {

    def getScannerAsync[A[_]](scan: Scan)(implicit backend: AsyncBackend[A]): A[ResultScanner] =
      TableUtils.asyncQuery[Scan, ResultScanner, A](scan, table.getScanner)

    def getAsync[A[_]](get: Get)(implicit backend: AsyncBackend[A]): A[Result] =
      TableUtils.asyncQuery[Get, Result, A](get, table.get)

    def putAsync[A[_]](put: Put)(implicit backend: AsyncBackend[A]): A[Unit] =
      TableUtils.asyncQuery[Put, Unit, A](put, table.put)

    def deleteAsync[A[_]](delete: Delete)(implicit backend: AsyncBackend[A]): A[Unit] =
      TableUtils.asyncQuery[Delete, Unit, A](delete, table.delete)
  }
}

object TableSyntax extends TableSyntax
