package com.svenvandam.htypes.hbase.result

import com.svenvandam.htypes.hbase.RowDecoder
import org.apache.hadoop.hbase.client.{Result, ResultScanner}

trait ResultSyntax {

  implicit class ResultOps(res: Result) {

    /**
      * Decode the values in [[Result]] to a sequence of time-ordered values of type `A`.
      * Each timed value represents what the object looked like at that point in time.
      */
    def as[T](implicit decoder: RowDecoder[T]): Seq[(T, Long)] = ResultUtils.resultAs(res)
  }
}

object ResultSyntax extends ResultSyntax
