package de.valoplus.color

/**
 * Created by tom on 17.04.16.
 */
interface StateVisitor<T> {
    fun visit(state: ColorStateWS2812): T
    fun visit(state: DummyState): T
}