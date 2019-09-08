package com.svenvandam.htypes

import com.svenvandam.htypes.async.AsyncTable
import com.svenvandam.htypes.converters.{ScalaConverter, MutationConverters, ResultConverters}

object Implicits
  extends AsyncTable
  with ScalaConverter
  with MutationConverters
  with ResultConverters
