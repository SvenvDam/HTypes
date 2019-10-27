package com.svenvandam.htypes.model

import com.svenvandam.htypes.codec.Encoder

case class CellValue(value: Array[Byte], timestamp: Option[Long] = None) {
  override def equals(that: Any) = that match {
    case CellValue(v, t) => v.toSeq == value.toSeq && t == timestamp
  }
}

object CellValue {
  def apply[A](value: A, timestamp: Option[Long])(implicit encoder: Encoder[Array[Byte], A]) =
    new CellValue(
      encoder.encode(value),
      timestamp
    )
}
