package de.valoplus.channel

import java.io.Serializable

/**
 * Created by tom on 12.02.16.
 */
class Channel : Serializable {
    var name: String? = null
    var type: String? = null
    var typedef: ChannelType? = null

    constructor(name: String, type: String, typedef: ChannelType) {
        this.name = name
        this.type = type
        this.typedef = typedef
    }

    constructor() {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Channel

        if (name != other.name) return false
        if (type != other.type) return false
        if (typedef != other.typedef) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result += 31 * result + (type?.hashCode() ?: 0)
        result += 31 * result + (typedef?.hashCode() ?: 0)
        return result
    }
}
