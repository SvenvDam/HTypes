package com.svenvandam.htypes.async

import org.apache.hadoop.hbase.client._
import scala.concurrent.{ExecutionContext, Future}

trait TableSyntax {

  implicit class TableOps(table: Table) {

    def getScannerAsync(scan: Scan)(implicit ec: ExecutionContext): Future[ResultScanner] =
      TableUtils.scanAsync(table)(scan)

    def getAsync(get: Get)(implicit ec: ExecutionContext): Future[Result] =
      TableUtils.getAsync(table)(get)

    def putAsync(put: Put)(implicit ec: ExecutionContext): Future[Unit] =
      TableUtils.putAsync(table)(put)

    def deleteAsync(delete: Delete)(implicit ec: ExecutionContext): Future[Unit] =
      TableUtils.deleteAsync(table)(delete)
  }
}

object TableSyntax extends TableSyntax
