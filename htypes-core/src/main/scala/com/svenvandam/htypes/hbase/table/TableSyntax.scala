package com.svenvandam.htypes.hbase.table

import com.svenvandam.htypes.effect.{EffectBackend, EffectUtils}
import org.apache.hadoop.hbase.client._

trait TableSyntax {

  /**
    * Syntax enrichment for [[Table]] which allows queries to be wrapped in an effect wrapper `A`
    */
  implicit class TableOps(table: Table) {

    def getEffect[A[_]](get: Get)(implicit backend: EffectBackend[A]): A[Result] =
      EffectUtils.lift(table.get(get))

    def putEffect[A[_]](put: Put)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.lift(table.put(put))

    def deleteEffect[A[_]](delete: Delete)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.lift(table.delete(delete))

    def appendEffect[A[_]](append: Append)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.lift(table.append(append))

    def incrementEffect[A[_]](increment: Increment)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.lift(table.increment(increment))
  }
}

object TableSyntax extends TableSyntax
