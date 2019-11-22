package com.svenvandam.htypes.effect

object EffectUtils {

  /**
    * Execute an effectful action and wrap it in an effect wrapper `E` using [[EffectBackend]].
    */
  def wrap[A, B[_]](a: => A)(implicit backend: EffectBackend[B]): B[A] =
    backend.wrap(a)

}
