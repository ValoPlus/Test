package de.valoplus.channel

/**
 * Created by tom on 18.02.16.
 */
class ChannelTypeWS2812 : ChannelType {
    var channelId: Int? = null
    var ledCount: Int? = null

    constructor(channelId: Int?, ledCount: Int?) {
        this.channelId = channelId
        this.ledCount = ledCount
    }

    constructor() {
    }

    override fun toString(): String {
        return "Channel: $channelId, $ledCount LED's"
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ChannelTypeWS2812

        if (channelId != other.channelId) return false
        if (ledCount != other.ledCount) return false

        return true
    }

    override fun hashCode(): Int{
        var result = channelId ?: 0
        result += 31 * result + (ledCount ?: 0)
        return result
    }

    override fun <T> accept(visitor: ChannelVisitor<T>): T {
        return visitor.visit(this)
    }
}
