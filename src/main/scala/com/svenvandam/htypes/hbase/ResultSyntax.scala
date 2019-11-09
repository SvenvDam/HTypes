package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.converters.ScalaConverter
import org.apache.hadoop.hbase.client.{Result, ResultScanner}

trait ResultSyntax extends ScalaConverter {

  implicit class ResultOps(res: Result) {

    /**
      * Decode the values in [[Result]] to a sequence of time-ordered values of type `A`.
      * Each timed value represents what the object looked like at that point in time.
      */
    def as[T](implicit decoder: RowDecoder[T]): Iterable[(T, Long)] = ResultUtils.as(res)
  }

  implicit class ResultScannerOps(res: ResultScanner) {

    /**
      * Decode the values in [[ResultScanner]] to a sequence of sequences of time-ordered values of type `A`.
      * There is one sequence for each row in the result.
      * Each timed value represents what the object looked like at that point in time.
      */
    def as[T](implicit decoder: RowDecoder[T]): Iterable[Iterable[(T, Long)]] =
      res.map(_.as[T])
  }
}

object ResultSyntax extends ResultSyntax
