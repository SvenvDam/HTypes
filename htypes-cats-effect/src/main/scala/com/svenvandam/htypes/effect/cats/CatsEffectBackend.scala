package com.svenvandam.htypes.effect.cats

import cats.effect.IO
import com.svenvandam.htypes.effect.EffectBackend

object CatsEffectBackend {

  /**
    * [[EffectBackend]] instance for [[IO]]
    */
  implicit val catsEffectBackend = new EffectBackend[IO] {
    def lift[A](a: => A): IO[A] = IO.delay(a)
  }
}
