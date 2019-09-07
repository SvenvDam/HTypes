package com.svenvdam.hbase.converters

import com.svenvdam.hbase.model.{CellValue, DecodedValue, Row, Column}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.util.Bytes
import scala.concurrent.{Future, ExecutionContext}
import scala.jdk.CollectionConverters._

object ResultDecoder {
  implicit class ResultOps(res: Result) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[DecodedValue[T]] = {
      val row = Bytes.toString(res.getRow)
      res
        .listCells()
        .asScala
        .foldLeft(List.empty[AccResult]) {
          case (lst, cell) =>
            val family = Bytes.toString(cell.getFamilyArray)
            val qualifier = Bytes.toString(cell.getQualifierArray)
            val value = cell.getValueArray
            val timestamp = cell.getTimestamp
            AccResult(
              timestamp,
              lst.head.values + (Column(family, qualifier) -> CellValue(value))
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

  implicit class ResultScannerOps(res: Iterable[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[Iterable[DecodedValue[T]]] =
      res.map(_.as[T])
  }

  implicit class FutureResultOps(f: Future[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[DecodedValue[T]]] = f
      .map(_.as[T])
  }

  implicit class FutureResultScannerOps(f: Future[Iterable[Result]]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[Iterable[DecodedValue[T]]]] = f
      .map(_.as[T])
  }
}
