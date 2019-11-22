package com.svenvandam.htypes.hbase.table

import com.svenvandam.htypes.effect.{EffectBackend, EffectUtils}
import org.apache.hadoop.hbase.client._

trait TableSyntax {

  /**
    * Syntax enrichment for [[Table]] which allows queries to be wrapped in an effect wrapper `A`
    */
  implicit class TableOps(table: Table) {

    def getAsync[A[_]](get: Get)(implicit backend: EffectBackend[A]): A[Result] =
      EffectUtils.wrap(table.get(get))

    def putAsync[A[_]](put: Put)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap(table.put(put))

    def deleteAsync[A[_]](delete: Delete)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap(table.delete(delete))

    def appendAsync[A[_]](append: Append)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap(table.append(append))

    def incrementAsync[A[_]](increment: Increment)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap(table.increment(increment))
  }
}

object TableSyntax extends TableSyntax
