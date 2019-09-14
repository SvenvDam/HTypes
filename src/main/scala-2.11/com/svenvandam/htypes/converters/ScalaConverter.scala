package com.svenvandam.htypes.converters

import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import scala.collection.JavaConverters._

trait ScalaConverter {
  implicit def resultToScala(res: ResultScanner): Iterable[Result] = res.asScala
}

object ScalaConverter extends ScalaConverter
