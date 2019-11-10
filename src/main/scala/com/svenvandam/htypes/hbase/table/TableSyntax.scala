package com.svenvandam.htypes.hbase.table

import com.svenvandam.htypes.effect.{EffectBackend, EffectUtils}
import org.apache.hadoop.hbase.client._

trait TableSyntax {

  /**
    * Syntax enrichment for [[Table]] which allows the most common queries to be ran in an effect wrapper `A`.
    */
  implicit class TableOps(table: Table) {

    def getScannerAsync[A[_]](scan: Scan)(implicit backend: EffectBackend[A]): A[ResultScanner] =
      EffectUtils.wrap[Scan, ResultScanner, A](table.getScanner)(scan)

    def getAsync[A[_]](get: Get)(implicit backend: EffectBackend[A]): A[Result] =
      EffectUtils.wrap[Get, Result, A](table.get)(get)

    def putAsync[A[_]](put: Put)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap[Put, Unit, A](table.put)(put)

    def deleteAsync[A[_]](delete: Delete)(implicit backend: EffectBackend[A]): A[Unit] =
      EffectUtils.wrap[Delete, Unit, A](table.delete)(delete)
  }
}

object TableSyntax extends TableSyntax
