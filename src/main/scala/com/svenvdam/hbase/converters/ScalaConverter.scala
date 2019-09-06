package com.svenvdam.hbase.converters

import java.util
import com.svenvdam.hbase.model.{Value, Column}
import org.apache.hadoop.hbase.client.{Result, ResultScanner}
import org.apache.hadoop.hbase.util.Bytes
import scala.jdk.CollectionConverters._

object ScalaConverter {
  implicit def resultToScala(res: ResultScanner): Iterable[Result] = res.asScala

  implicit def valuesToScala(
      values: util.NavigableMap[Array[Byte], util.NavigableMap[Array[Byte], util.NavigableMap[Long, Array[Byte]]]]
  ): Map[Column, Value] = {
    val scalafied = values.asScala.view.mapValues(_.asScala.view.mapValues(_.asScala.view.toMap).toMap).toMap
    for {
      (familyB, map1) <- scalafied
      (qualifierB, map2) <- map1
      (time, valueB) <- map2
    } yield {
      val family = Bytes.toString(familyB)
      val qualifier = Bytes.toString(qualifierB)
      (Column(family, qualifier), Value(valueB, Some(time)))
    }
  }
}
