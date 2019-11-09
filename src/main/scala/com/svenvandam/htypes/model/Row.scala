package com.svenvandam.htypes.model

import com.svenvandam.htypes.bytes.ByteEncoder

/**
  * HTypes encoded representation of an object.
  * It contains a key as byte array and a mapping from [[Column]] to [[CellValue]].
  *
  * [[com.svenvandam.htypes.hbase.RowEncoder]] and [[com.svenvandam.htypes.hbase.RowDecoder]] will use this class as the encoded form.
  * HTypes handles storing all information in a [[Row]] and constructing them using query results.
  */
case class Row(key: Array[Byte], values: Map[Column, CellValue]) {
  override def equals(that: Any) = that match {
    case Row(k, vals) => k.toSeq == key.toSeq && vals == values
    case _            => false
  }
}

object Row {

  /**
    * Construct a new instance. The key is encoded using a [[ByteEncoder]].
    */
  def apply[A](key: A, values: Map[Column, CellValue])(implicit encoder: ByteEncoder[A]) =
    new Row(
      encoder.encode(key),
      values
    )
}
