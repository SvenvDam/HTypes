package com.svenvandam.htypes.codec

trait HBaseCodec[T] extends HBaseEncoder[T] with HBaseDecoder[T]
