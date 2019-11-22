package com.svenvandam.htypes.model

import com.svenvandam.htypes.bytes.ByteEncoder

/**
  * Class that represents a combination of a column family and qualifier.
  */
case class Column(family: Array[Byte], qualifier: Array[Byte]) {
  override def equals(that: Any): Boolean = that match {
    case Column(fam, qual) => fam.toSeq == family.toSeq && qual.toSeq == qualifier.toSeq
    case _                 => false
  }
}

object Column {

  /**
    * Construct a new [[Column]]. Column family and qualifier are encoded as byte arrays using [[ByteEncoder]]s
    */
  def apply[A, B](family: A, qualifier: B)(implicit familyEncoder: ByteEncoder[A], qualifierEnCoder: ByteEncoder[B]) =
    new Column(
      familyEncoder.encode(family),
      qualifierEnCoder.encode(qualifier)
    )

}
