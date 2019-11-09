package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.Result

object ResultUtils {

  // we want to process values oldest to newest
  implicit private val longOrdering = new Ordering[Long] {
    def compare(x: Long, y: Long) = y compare x
  }

  /**
    * Converts a Result to an Iterable of decoded form A and timestamp.
    * The returned Iterable will be ordered from new to old.
    * @param result a Result containing  data from HBase
    * @param decoder RowDecoder instance to construct an A from a Row
    * @tparam A the type to be decoded to
    * @return Iterable, ordered from new to old, containing decoded objects
    */
  def as[A](result: Result)(implicit decoder: RowDecoder[A]): Iterable[(A, Long)] = {
    val row = result.getRow
    result
      .rawCells
      .groupBy(_.getTimestamp)
      .toSeq
      .sortBy(_._1)
      .scanRight((Map.empty[Column, CellValue], 0: Long)) {
        case ((timestamp, newCells), (columns, _)) =>
          val newColumns = newCells.map(getColumnAndValueFromCell)

          (columns ++ newColumns, timestamp)
      }
      .map {
        case (columns, timestamp) =>
          (decoder.decode(Row(row, columns)), timestamp)
      }
      .flatMap {
        case (Some(value), timestamp) => Iterable((value, timestamp))
        case _                        => Iterable.empty
      }
  }

  private def getColumnAndValueFromCell(cell: Cell) = {
    val column = Column(
      CellUtil.cloneFamily(cell),
      CellUtil.cloneQualifier(cell)
    )

    val cellValue = CellValue(
      CellUtil.cloneValue(cell),
      Some(cell.getTimestamp)
    )

    column -> cellValue
  }

}
