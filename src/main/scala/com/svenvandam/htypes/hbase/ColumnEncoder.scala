package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Column

trait ColumnEncoder[A] {
  def getColumns: Set[Column]
}
