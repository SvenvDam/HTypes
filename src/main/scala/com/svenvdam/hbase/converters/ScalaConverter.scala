package com.svenvdam.hbase.converters

import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import scala.jdk.CollectionConverters._

object ScalaConverter {
  implicit def resultToScala(res: ResultScanner): Iterable[Result] = res.asScala
}
