package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowDecoder
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.Result

object ResultUtils {

  implicit private val cellOrdering = new Ordering[Cell] {
    override def compare(x: Cell, y: Cell) =
      x.getTimestamp.compare(y.getTimestamp)
  }

  def as[T](result: Result)(implicit decoder: RowDecoder[T]): Iterable[(T, Long)] = {
    val row = result.getRow
    result
      .rawCells
      .groupBy(_.getTimestamp)
      .toSeq
      .sortBy(_._1)
      .scanLeft[(Map[Column, CellValue], Long)]((Map.empty, 0)) {
        case ((columns, _), (timestamp, newCells)) =>
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
