package de.valoplus.channel

import java.io.Serializable

/**
 * Created by tom on 18.02.16.
 */
interface ChannelType : Serializable {
    fun <T> accept(visitor: ChannelVisitor<T>): T
}
