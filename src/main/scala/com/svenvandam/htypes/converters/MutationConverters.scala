package com.svenvandam.htypes.converters

import java.util
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.{Put, Delete}
import scala.jdk.CollectionConverters._

trait MutationConverters {
  implicit def toPut[T](t: T)(implicit encoder: HBaseEncoder[T]): Put = {
    val row = encoder.encode(t)
    row.values
      .foldLeft(new Put(row.getKeyB)) {
        case (put, (col, CellValue(v, Some(time)))) =>
          put.addColumn(col.getFamilyB, col.getQualifierB, time, v)

        case (put, (col, CellValue(v, None))) =>
          put.addColumn(col.getFamilyB, col.getQualifierB, v)
      }
  }

  implicit def toPuts[T](lst: Iterable[T])(implicit encoder: HBaseEncoder[T]): util.List[Put] =
    lst.map(toPut(_)(encoder)).toList.asJava

  // TODO: what if you want to delete columns matching a specific value?
  implicit def toDelete[T](t: T)(implicit encoder: HBaseEncoder[T]): Delete = {
    val row = encoder.encode(t)
    row.values.foldLeft(new Delete(row.getKeyB)) {
      case (del, (col, CellValue(_, Some(time)))) =>
        del.addColumn(col.getFamilyB, col.getQualifierB, time)

      case (del, (col, CellValue(_, None))) =>
        del.addColumn(col.getFamilyB, col.getQualifierB)
    }
  }

  implicit def toDeletes[T](lst: Iterable[T])(implicit encoder: HBaseEncoder[T]): util.List[Delete] =
    new util.LinkedList(lst.map(toDelete(_)(encoder)).toList.asJava)
}

object MutationConverters extends MutationConverters