package com.svenvandam.htypes.converters

trait HBaseCodec[T] extends HBaseEncoder[T] with HBaseDecoder[T]
