package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowDecoder
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.Result

object ResultUtils {

  // we want to process values oldest to newest
  implicit private val longOrdering = new Ordering[Long] {
    def compare(x: Long, y: Long) = y compare x
  }

  def as[T](result: Result)(implicit decoder: RowDecoder[T]): Iterable[(T, Long)] = {
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
        case (Some(value), timestamp) => List((value, timestamp))
        case _                        => List.empty
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
