package com.svenvandam.htypes.async

trait AsyncBackend[A[_]] {
  def makeAsync[B](b: => B): A[B]
}
