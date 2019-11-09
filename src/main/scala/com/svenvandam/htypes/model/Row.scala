package com.svenvandam.htypes.model

import com.svenvandam.htypes.bytes.ByteEncoder

case class Row(key: Array[Byte], values: Map[Column, CellValue]) {
  override def equals(that: Any) = that match {
    case Row(k, vals) => k.toSeq == key.toSeq && vals == values
    case _            => false
  }
}

object Row {
  def apply[A](key: A, values: Map[Column, CellValue])(implicit encoder: ByteEncoder[A]) =
    new Row(
      encoder.encode(key),
      values
    )
}
