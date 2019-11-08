package com.svenvandam.htypes.async

import org.apache.hadoop.hbase.client._

object TableUtils {
  def getAsync[A[_]](table: Table)(get: Get)(implicit backend: AsyncBackend[A]): A[Result] =
    backend.makeAsync(table.get(get))

  def scanAsync[A[_]](table: Table)(scan: Scan)(implicit backend: AsyncBackend[A]): A[ResultScanner] =
    backend.makeAsync(table.getScanner(scan))

  def putAsync[A[_]](table: Table)(put: Put)(implicit backend: AsyncBackend[A]): A[Unit] =
    backend.makeAsync(table.put(put))

  def deleteAsync[A[_]](table: Table)(delete: Delete)(implicit backend: AsyncBackend[A]): A[Unit] =
    backend.makeAsync(table.delete(delete))
}
