package com.svenvandam.htypes.hbase.result

import com.svenvandam.htypes.converters.ScalaConverter
import com.svenvandam.htypes.hbase.RowDecoder
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.{Result, ResultScanner}

object ResultUtils extends ScalaConverter {

  // we want to process values oldest to newest
  implicit private val longOrdering = new Ordering[Long] {
    def compare(x: Long, y: Long) = y compare x
  }

  /**
    * Converts a [[Result]] to a [[Seq]] of decoded form A and timestamp.
    * The returned sequence will be ordered from new to old.
    */
  def resultAs[A](result: Result)(implicit decoder: RowDecoder[A]): Seq[(A, Long)] = {
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

  def resultScannerAs[A](resultScanner: ResultScanner)(implicit decoder: RowDecoder[A]): Seq[Seq[(A, Long)]] =
    resultScanner.toSeq.map(resultAs[A])

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
