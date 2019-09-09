package com.svenvandam.htypes

import com.svenvandam.htypes.async.AsyncTable
import com.svenvandam.htypes.converters.{ScalaConverter, ResultSyntax, QuerySyntax}

object Implicits
  extends AsyncTable
  with ScalaConverter
  with QuerySyntax
  with ResultSyntax
