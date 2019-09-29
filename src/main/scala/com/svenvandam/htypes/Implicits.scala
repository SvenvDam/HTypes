package com.svenvandam.htypes

import com.svenvandam.htypes.async.TableSyntax
import com.svenvandam.htypes.codec.Transformers
import com.svenvandam.htypes.converters.{QuerySyntax, ResultSyntax, ScalaConverter}

object Implicits extends TableSyntax with ScalaConverter with QuerySyntax with ResultSyntax with Transformers
