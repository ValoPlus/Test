package de.valoplus.color

/**
 * Created by tom on 12.04.16.
 */
class DummyState() : State(brightness = 0) {
    override fun <T> accept(visitor: StateVisitor<T>): T {
        return visitor.visit(this)
    }
}