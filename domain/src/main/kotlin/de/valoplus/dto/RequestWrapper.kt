package de.valoplus.dto

/**
 * This Wrapper is used to add a clientId to the request.
 */
class RequestWrapper<T> {
    var clientId: String? = null
    var content: T? = null

    constructor() {

    }

    constructor(clientId: String?, content: T?) {
        this.clientId = clientId
        this.content = content
    }
}