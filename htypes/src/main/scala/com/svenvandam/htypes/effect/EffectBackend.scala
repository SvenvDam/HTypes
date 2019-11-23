package com.svenvandam.htypes.effect

/**
  * Type class to wrap side-effects.
  * Think of Future, Task, IO, etc...
  */
trait EffectBackend[A[_]] {
  def lift[B](b: => B): A[B]
}
