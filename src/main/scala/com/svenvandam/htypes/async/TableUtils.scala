package com.svenvandam.htypes.async

import org.apache.hadoop.hbase.client._
import scala.concurrent.{blocking, ExecutionContext, Future}

object TableUtils {
  def getAsync[A](table: Table)(get: Get)(implicit ec: ExecutionContext): Future[Result] =
    makeAsync(table.get(get))

  def scanAsync[A](table: Table)(scan: Scan)(implicit ec: ExecutionContext): Future[ResultScanner] =
    makeAsync(table.getScanner(scan))

  def putAsync[A](table: Table)(put: Put)(implicit ec: ExecutionContext): Future[Unit] =
    makeAsync(table.put(put))

  def deleteAsync[A](table: Table)(delete: Delete)(implicit ec: ExecutionContext): Future[Unit] =
    makeAsync(table.delete(delete))

  private[this] def makeAsync[T](t: => T)(implicit ec: ExecutionContext): Future[T] =
    Future {
      blocking {
        t
      }
    }
}
