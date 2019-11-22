package com.svenvandam.htypes.hbase.result

import com.svenvandam.htypes.converters.ScalaConverter
import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import com.svenvandam.htypes.stream.StreamBackend

trait ResultScannerSyntax extends ScalaConverter {
  implicit class ResultScannerOps(scanner: ResultScanner) {

    def stream[A[_]](implicit backend: StreamBackend[A]): A[Result] =
      ResultScannerUtils.streamResults(scanner)
  }
}

object ResultScannerSyntax extends ResultScannerSyntax
