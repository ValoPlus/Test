package de.valoplus.channel

/**
 * Created by tom on 18.02.16.
 */
class ChannelTypeRGB : ChannelType {
    var red: Int? = null
    var green: Int? = null
    var blue: Int? = null

    constructor(red: Int?, green: Int?, blue: Int?) {
        this.red = red
        this.green = green
        this.blue = blue
    }

    constructor() {
    }

    override fun toString(): String {
        return "RED: $red, GREEN: $green, BLUE: $blue"
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ChannelTypeRGB

        if (red != other.red) return false
        if (green != other.green) return false
        if (blue != other.blue) return false

        return true
    }

    override fun hashCode(): Int{
        var result = red ?: 0
        result += 31 * result + (green ?: 0)
        result += 31 * result + (blue ?: 0)
        return result
    }

    override fun <T> accept(visitor: ChannelVisitor<T>): T {
        return visitor.visit(this)
    }
}
