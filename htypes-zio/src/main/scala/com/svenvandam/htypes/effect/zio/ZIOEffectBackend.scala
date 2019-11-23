package com.svenvandam.htypes.effect.zio

import com.svenvandam.htypes.effect.EffectBackend
import zio.Task

object ZIOEffectBackend {

  /**
    * [[EffectBackend]] instance for [[Task]]
    */
  implicit val zioTaskEffectBackend = new EffectBackend[Task] {
    def lift[A](a: => A): Task[A] = Task(a)
  }
}
