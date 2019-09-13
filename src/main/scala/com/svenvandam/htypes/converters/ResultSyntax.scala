package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseDecoder
import com.svenvandam.htypes.model.{CellValue, Row, Column}
import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hbase.{CellUtil, Cell}
import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import org.apache.hadoop.hbase.util.Bytes
import scala.concurrent.{Future, ExecutionContext}

trait ResultSyntax extends ScalaConverter {

  private implicit val cellOrdering = new Ordering[Cell] {
    override def compare(x: Cell, y: Cell) =
      x.getTimestamp.compare(y.getTimestamp)
  }

  implicit class ResultOps(res: Result) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[(T, Long)] = {
      val row = Bytes.toString(res.getRow)
      res
        .rawCells
        .sorted
        .foldLeft(List.empty[AccResult]) {
          case (lst, cell) =>
            val family = Bytes.toString(CellUtil.cloneFamily(cell))
            val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
            val value = CellUtil.cloneValue(cell)
            val timestamp = cell.getTimestamp
            val head = lst.headOption.getOrElse(AccResult(0, Map.empty))
            val tail = if (head.timestamp == timestamp) lst.tail else lst
            AccResult(
              timestamp,
              head.values + (Column(family, qualifier) -> CellValue(value))
            ) :: tail
        }
        .map(acc => (decoder.decode(Row(row, acc.values)), acc.timestamp))
        .flatMap {
          case (Some(value), timestamp) => List((value, timestamp))
          case _ => List.empty
        }
    }

    private[this] case class AccResult(timestamp: Long, values: Map[Column, CellValue])
  }

  implicit class ResultScannerOps(res: ResultScanner) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[Iterable[(T, Long)]] =
      res.map(_.as[T])
  }

  implicit class FutureResultOps(f: Future[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[(T, Long)]] = f
      .map(_.as[T])
  }

  implicit class FutureResultScannerOps(f: Future[ResultScanner]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[Iterable[(T, Long)]]] = f
      .map(_.as[T])
  }
}

object ResultSyntax extends ResultSyntax
