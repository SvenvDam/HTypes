package com.svenvandam.htypes.effect.cats

import org.scalatest.FunSuite
import com.svenvandam.htypes.effect.EffectUtils
import org.scalatest.Matchers._

class CatsEffectBackendTest extends FunSuite {
  import CatsEffectBackend._

  test("it should delay a side effect") {
    var x = 0

    def action = x = 1

    val io = EffectUtils.lift(action)

    x shouldBe 0

    io.unsafeRunSync()

    x shouldBe 1
  }
}
