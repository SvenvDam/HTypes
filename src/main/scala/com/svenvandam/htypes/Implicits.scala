package com.svenvandam.htypes

import com.svenvandam.htypes.bytes.{ByteCodecInstances, ByteSyntax}
import com.svenvandam.htypes.converters.ScalaConverter
import com.svenvandam.htypes.hbase.query.QuerySyntax
import com.svenvandam.htypes.hbase.result.ResultSyntax
import com.svenvandam.htypes.hbase.table.TableSyntax

object Implicits
    extends TableSyntax
    with ScalaConverter
    with QuerySyntax
    with ResultSyntax
    with ByteSyntax
    with ByteCodecInstances
