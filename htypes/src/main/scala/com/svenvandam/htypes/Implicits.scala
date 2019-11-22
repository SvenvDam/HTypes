package com.svenvandam.htypes

import com.svenvandam.htypes.async.CompletableFutureSyntax
import com.svenvandam.htypes.bytes.{ByteSyntax, ByteCodecInstances}
import com.svenvandam.htypes.converters.ScalaConverter
import com.svenvandam.htypes.hbase.query.QuerySyntax
import com.svenvandam.htypes.hbase.result.{ResultSyntax, ResultScannerSyntax}
import com.svenvandam.htypes.hbase.table.TableSyntax

object Implicits
  extends TableSyntax
  with ScalaConverter
  with QuerySyntax
  with ResultSyntax
  with ResultScannerSyntax
  with ByteSyntax
  with ByteCodecInstances
  with CompletableFutureSyntax
