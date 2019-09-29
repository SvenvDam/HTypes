package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseDecoder
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import org.apache.hadoop.hbase.util.Bytes
import scala.concurrent.{ExecutionContext, Future}

trait ResultSyntax extends ScalaConverter {

  implicit class ResultOps(res: Result) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[(T, Long)] = ResultUtils.as(res)
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
