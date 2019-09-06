package com.svenvdam.hbase.converters

import org.apache.hadoop.hbase.client.Result
import scala.concurrent.{Future, ExecutionContext}

// TODO: handle multiple cells per column
object ResultDecoder {
  implicit class ResultOps(res: Result) {
    def as[T](implicit decoder: HBaseDecoder[T]): T = decoder.decode(res)
  }

  implicit class ResultScannerOps(res: Iterable[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T]): Iterable[T] = res.map(_.as[T])
  }

  implicit class FutureResultOps(f: Future[Result]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[T] = f.map(_.as[T])
  }

  implicit class FutureResultScannerOps(f: Future[Iterable[Result]]) {
    def as[T](implicit decoder: HBaseDecoder[T], ec: ExecutionContext): Future[Iterable[T]] = f.map(_.as[T])
  }
}
