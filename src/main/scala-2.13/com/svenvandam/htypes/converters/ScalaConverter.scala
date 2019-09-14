package com.svenvandam.htypes.converters

import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import scala.collection.convert._

trait ScalaConverter extends AsScalaExtensions {
  implicit def resultToScala(res: ResultScanner): Iterable[Result] = res.asScala
}

object ScalaConverter extends ScalaConverter
