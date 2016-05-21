package de.valoplus.controller

import de.valoplus.channel.*
import de.valoplus.wlan.Wlan
import java.util.*

/**
 * Created by tom on 12.02.16.
 */
open class Controller() {
    val channel: MutableList<Channel>
    val status: MutableList<Status>
    var availableChannel = 8
    var wlan: Wlan? = null
    var controllerAlias: String? = null
    var controllerKey: String? = null
    var controllerIp: String? = null

    constructor(availableChannel: Long, type: String, controllerAlias: String, controllerKey: String, controllerIp: String) : this() {
        this.controllerAlias = controllerAlias
        this.controllerKey = controllerKey
        this.controllerIp = controllerIp
        this.availableChannel = availableChannel.toInt()
    }

    init {
        this.channel = ArrayList()
        this.status = ArrayList()
    }

    val type = "ESP8266"

    fun isNameFree(name: String) : Boolean {
        this.channel.forEach {
            if(it.name == name) {
                return false
            }
        }
        return true
    }

    fun isChannelFree(id: Int) : Boolean {
        if (id < 1 || id > availableChannel) {
            return false
        }
        this.channel.forEach {
            if (it.typedef!!.accept(object : ChannelVisitor<Boolean> {
                override fun visit(channel: ChannelTypeRGB): Boolean {
                    if(channel.blue == id || channel.red == id || channel.green == id) {
                        return true
                    }
                    return false
                }

                override fun visit(channel: ChannelTypeSingleColor): Boolean {
                    return channel.channelId == id
                }

                override fun visit(channel: ChannelTypeWS2812): Boolean {
                    return channel.channelId == id
                }
            })) {
                return false
            }
        }
        return true
    }

    fun activeChannels() : Int {
        return status.filter { it.active }.size
    }
}
