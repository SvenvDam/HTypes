package com.svenvandam.htypes.effect.zio

import com.svenvandam.htypes.effect.EffectUtils
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import ZIOEffectBackend._
import zio.DefaultRuntime

class ZIOEffectBackendTest extends FunSuite {
  test("it should delay side effect") {
    var x = 0

    def action = x = 1

    val io = EffectUtils.lift(action)

    x shouldBe 0

    val runtime = new DefaultRuntime {}

    runtime.unsafeRun(io)

    x shouldBe 1
  }
}
