package com.svenvdam.hbase.async

import org.apache.hadoop.hbase.client._
import scala.concurrent.{Future, ExecutionContext, blocking}

object AsyncTable {

  implicit class TableOps(table: Table) {

    def scanAsync(s: Scan)(implicit ec: ExecutionContext): Future[ResultScanner] = makeAsync(table.getScanner(s))

    def getAsync(g: Get)(implicit ec: ExecutionContext): Future[Result] = makeAsync(table.get(g))

    def putAsync(p: Put)(implicit ec: ExecutionContext): Future[Unit] = makeAsync(table.put(p))

    def deleteAsync(d: Delete)(implicit ec: ExecutionContext): Future[Unit] = makeAsync(table.delete(d))
  }

  private[this] def makeAsync[T](t: => T)(implicit ec: ExecutionContext): Future[T] =
    Future {
      blocking {
        t
      }
    }
}
