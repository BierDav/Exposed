package org.jetbrains.exposed.v1.core.statements.api

import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.readByteArray

/** Represents a wrapper for an [inputStream] of bytes to be used in binary columns. */
class ExposedBlob(inputStream: Source) {
    constructor(bytes: ByteArray) : this (Buffer().apply { write(bytes) })

    /** The [InputStream] contained by this wrapper. */
    var inputStream = inputStream
        private set

    /** The `ByteArray` returned as a result of reading the contained [InputStream] completely. */
    val bytes: ByteArray
        get() = inputStream.peek().readByteArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExposedBlob) return false

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int = bytes.contentHashCode()

    /** Returns the hex-encoded string of the contained [InputStream] after being read. */
    @OptIn(ExperimentalStdlibApi::class)
    fun hexString(): String = bytes.toHexString()
}
