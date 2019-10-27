package com.svenvandam.htypes.model

import com.svenvandam.htypes.codec.Encoder

case class Row(key: Array[Byte], values: Map[Column, CellValue])

object Row {
  def apply[A](key: A, values: Map[Column, CellValue])(implicit encoder: Encoder[Array[Byte], A]) =
    new Row(
      encoder.encode(key),
      values
    )
}
