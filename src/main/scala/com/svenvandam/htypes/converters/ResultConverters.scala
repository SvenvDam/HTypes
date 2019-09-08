package com.svenvandam.htypes.converters

import com.svenvandam.htypes.model.{CellValue, DecodedValue, Row, Column}
import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import org.apache.hadoop.hbase.util.Bytes
import scala.concurrent.{Future, ExecutionContext}
import scala.jdk.CollectionConverters._

trait ResultConverters {
  implicit class ResultOps(res: Result) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[DecodedValue[T]] = {
      val row = Bytes.toString(res.getRow)
      res
        .listCells()
        .asScala
        .toSeq
        .foldRight(List.empty[AccResult]) {
          case (cell, lst) =>
            val family = Bytes.toString(CellUtil.cloneFamily(cell))
            val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
            val value = CellUtil.cloneValue(cell)
            val timestamp = cell.getTimestamp
            AccResult(
              timestamp,
              lst.headOption.getOrElse(AccResult(0, Map.empty)).values + (Column(family, qualifier) -> CellValue(value))
            ) :: lst
        }
        .map(acc => (decoder.decode(Row(row, acc.values)), acc.timestamp))
        .flatMap {
          case (Some(value), timestamp) => List(DecodedValue(value, timestamp))
          case _ => List.empty
        }
    }

    private[this] case class AccResult(timestamp: Long, values: Map[Column, CellValue])
  }

  implicit class ResultScannerOps(res: ResultScanner) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[Iterable[DecodedValue[T]]] =
      res.asScala.map(_.as[T])
  }

  implicit class FutureResultOps(f: Future[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[DecodedValue[T]]] = f
      .map(_.as[T])
  }

  implicit class FutureResultScannerOps(f: Future[ResultScanner]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[Iterable[DecodedValue[T]]]] = f
      .map(_.as[T])
  }
}

object ResultConverters extends ResultConverters
