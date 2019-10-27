package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Row
import com.svenvandam.htypes.codec.Encoder

trait RowEncoder[A] extends Encoder[Row, A]
