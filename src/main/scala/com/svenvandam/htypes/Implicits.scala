package com.svenvandam.htypes

import com.svenvandam.htypes.async.TableSyntax
import com.svenvandam.htypes.bytes.{ByteSyntax, Instances}
import com.svenvandam.htypes.converters.{QuerySyntax, ResultSyntax, ScalaConverter}

object Implicits
    extends TableSyntax
    with ScalaConverter
    with QuerySyntax
    with ResultSyntax
    with ByteSyntax
    with Instances
