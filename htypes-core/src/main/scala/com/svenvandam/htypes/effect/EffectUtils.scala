package com.svenvandam.htypes.effect

object EffectUtils {

  /**
    * Execute an effectful action and wrap it in an effect wrapper `E` using [[EffectBackend]].
    */
  def lift[A, B[_]](a: => A)(implicit effect: EffectBackend[B]): B[A] =
    effect.lift(a)

}
