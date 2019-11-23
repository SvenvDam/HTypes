package com.svenvandam.htypes.hbase.result

import com.svenvandam.htypes.converters.ScalaConverter
import com.svenvandam.htypes.stream.StreamBackend
import org.apache.hadoop.hbase.client.{ResultScanner, Result}

object ResultScannerUtils extends ScalaConverter {
  def streamResults[A[_]](scanner: ResultScanner)(implicit backend: StreamBackend[A]): A[Result] =
    backend.getStream(scanner.iterator().asScala)
}
