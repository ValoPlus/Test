package de.valoplus.channel

/**
 * Created by tom on 18.02.16.
 */
class ChannelTypeSingleColor : ChannelType {
    var channelId: Int? = null

    constructor(channelId: Int?) {
        this.channelId = channelId
    }

    constructor() {
    }

    override fun toString(): String {
        return "Channel: $channelId"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ChannelTypeSingleColor

        if (channelId != other.channelId) return false

        return true
    }

    override fun hashCode(): Int {
        return channelId ?: 0
    }

    override fun <T> accept(visitor: ChannelVisitor<T>): T {
        return visitor.visit(this)
    }
}
