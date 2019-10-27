package com.svenvandam.htypes.model

import com.svenvandam.htypes.codec.Encoder

case class Row(key: Array[Byte], values: Map[Column, CellValue]) {
  override def equals(that: Any) = that match {
    case Row(k, vals) => k.toSeq == key.toSeq && vals == values
    case _            => false
  }
}

object Row {
  def apply[A](key: A, values: Map[Column, CellValue])(implicit encoder: Encoder[Array[Byte], A]) =
    new Row(
      encoder.encode(key),
      values
    )
}
