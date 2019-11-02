package com.svenvandam.htypes.converters

import com.svenvandam.htypes.hbase.RowEncoder
import com.svenvandam.htypes.model.CellValue
import org.apache.hadoop.hbase.client.Put

object PutUtils {

  /**
    * Bind all information of an A to a Put query
    * @param put the Put query to bind info to
    * @param a object of type A containing info that must be stored
    * @param encoder RowEncoder instance for type A
    * @tparam A the type of the object to be encoded and stored
    * @return Put query with info bound to it
    */
  def from[A](put: Put)(a: A)(implicit encoder: RowEncoder[A]): Put =
    encoder
      .encode(a)
      .values
      .foldLeft(put) {
        case (p, (col, CellValue(value, Some(timestamp)))) =>
          p.addColumn(col.family, col.qualifier, timestamp, value)
        case (p, (col, CellValue(value, None))) =>
          p.addColumn(col.family, col.qualifier, value)
      }

  /**
    * Creates new Put query and binds all info of t to it.
    * @param a object of type A containing info that must be stored
    * @param encoder RowEncoder instance for type A
    * @tparam A the type of the object to be encoded and stored
    * @return new Put query with info bound to it
    */
  def createFrom[A](a: A)(implicit encoder: RowEncoder[A]): Put = {
    val row = encoder.encode(a)
    from(new Put(row.key))(a)
  }
}
