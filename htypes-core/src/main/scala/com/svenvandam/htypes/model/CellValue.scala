package com.svenvandam.htypes.model

import com.svenvandam.htypes.bytes.ByteEncoder

/**
  * Class that represents a value and timestamp combination.
  * Value is already encoded to a byte array.
  * If timestamp is [[None]], HBase will handle the insertion of timestamps.
  */
case class CellValue(value: Array[Byte], timestamp: Option[Long] = None) {
  override def equals(that: Any) = that match {
    case CellValue(v, t) => v.toSeq == value.toSeq && t == timestamp
  }
}

object CellValue {

  /**
    * Construct a new [[CellValue]]. The value will encoded to a byte array using a [[ByteEncoder]] instance.
    */
  def apply[A](value: A, timestamp: Option[Long])(implicit encoder: ByteEncoder[A]) =
    new CellValue(
      encoder.encode(value),
      timestamp
    )
}
