package de.valoplus.color

/**
 * Created by tom on 04.04.16.
 */
abstract class State(
        var brightness: Int
) {
    abstract fun <T> accept(visitor: StateVisitor<T>): T
}