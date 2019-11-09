package com.svenvandam.htypes.bytes

import org.apache.hadoop.hbase.util.Bytes

/**
  * [[ByteEncoder]] and [[ByteDecoder]] instances for primitives.
  */
trait ByteCodecInstances {
  implicit val identityByteCodec = ByteCodec.safeApply[Array[Byte]](identity, identity)
  implicit val stringByteCodec = ByteCodec.safeApply[String](Bytes.toBytes, Bytes.toString)
  implicit val boolByteCodec = ByteCodec.safeApply[Boolean](Bytes.toBytes, Bytes.toBoolean)
  implicit val intByteCodec = ByteCodec.safeApply[Int](Bytes.toBytes, Bytes.toInt)
  implicit val longByteCodec = ByteCodec.safeApply[Long](Bytes.toBytes, Bytes.toLong)
  implicit val floatByteCodec = ByteCodec.safeApply[Float](Bytes.toBytes, Bytes.toFloat)
  implicit val doubleByteCodec = ByteCodec.safeApply[Double](Bytes.toBytes, Bytes.toDouble)
  implicit val shortByteCodec = ByteCodec.safeApply[Short](Bytes.toBytes, Bytes.toShort)
}

object ByteCodecInstances extends ByteCodecInstances
