package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.Column

trait HBaseClassEncoder[T] {
  def getColumns: Set[Column]
}
