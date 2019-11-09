package com.svenvandam.htypes

import com.svenvandam.htypes.async.TableSyntax
import com.svenvandam.htypes.bytes.{ByteSyntax, Instances}
import com.svenvandam.htypes.converters.ScalaConverter
import com.svenvandam.htypes.hbase.{QuerySyntax, ResultSyntax}

object Implicits
    extends TableSyntax
    with ScalaConverter
    with QuerySyntax
    with ResultSyntax
    with ByteSyntax
    with Instances
