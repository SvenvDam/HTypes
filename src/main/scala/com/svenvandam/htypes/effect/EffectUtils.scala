package com.svenvandam.htypes.effect

object EffectUtils {

  /**
    * Execute an effectful action and wrap it in an effect wrapper `E` using [[EffectBackend]].
    *
    * @tparam I input type of action
    * @tparam R result type of action
    * @tparam E effect wrapper type
    */
  def wrap[I, R, E[_]](action: I => R)(input: I)(implicit backend: EffectBackend[E]): E[R] =
    backend.wrap(action(input))

}
