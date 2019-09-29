package com.svenvandam.htypes.converters

import com.svenvandam.htypes.codec.HBaseDecoder
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.util.Bytes

object ResultUtils {

  implicit private val cellOrdering = new Ordering[Cell] {
    override def compare(x: Cell, y: Cell) =
      x.getTimestamp.compare(y.getTimestamp)
  }

  def as[T](res: Result)(implicit decoder: HBaseDecoder[T]): Iterable[(T, Long)] = {
    val row = Bytes.toString(res.getRow)
    res
      .rawCells
      .sorted
      .foldLeft(List.empty[AccResult]) {
        case (lst, cell) =>
          val family = Bytes.toString(CellUtil.cloneFamily(cell))
          val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
          val value = CellUtil.cloneValue(cell)
          val timestamp = cell.getTimestamp
          val head = lst.headOption.getOrElse(AccResult(0, Map.empty))
          val tail = if (head.timestamp == timestamp) lst.tail else lst
          AccResult(
            timestamp,
            head.values + (Column(family, qualifier) -> CellValue(value))
          ) :: tail
      }
      .map(acc => (decoder.decode(Row(row, acc.values)), acc.timestamp))
      .flatMap {
        case (Some(value), timestamp) => List((value, timestamp))
        case _                        => List.empty
      }
  }

  private[this] case class AccResult(timestamp: Long, values: Map[Column, CellValue])
}
