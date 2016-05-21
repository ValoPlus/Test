package de.valoplus.color

/**
 * Created by tom on 04.04.16.
 */
class ColorStateWS2812(
        brightness: Int,
        var red: Int,
        var green: Int,
        var blue: Int
) : State(brightness) {
    override fun <T> accept(visitor: StateVisitor<T>): T {
        return visitor.visit(this)
    }
}
