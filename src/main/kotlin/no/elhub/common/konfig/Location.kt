package no.elhub.common.konfig

import java.io.File
import java.net.URI

/**
 * Describes the location of configuration information.  A location may have a [uri] or may not, because it is
 * compiled into the application or obtained from ephemeral data, such as the process environment or command-line
 * parameters,
 */
data class Location(val description: String, val uri: URI? = null) {
    constructor(file: File) : this(file.absolutePath, file.toURI())

    constructor(uri: URI) : this(uri.toString(), uri)

    companion object {
        /**
         * Describes the location of configuration data that is compiled into the application as code that creates
         * a [Configuration] object.
         *
         * Consider using the [intrinsic] function instead, which returns more descriptive Locations for intrinsic
         * configurations.
         */
        val INTRINSIC = Location("intrinsic")

        /**
         * Describes the location of configuration data that is generated by code, identifying the location in the code
         * that creates the configuration if possible.
         */
        fun intrinsic(prefix: String = "intrinsic") =
            Location(prefix + (callingStackFrame()?.let { ": $it" } ?: ""))

        private fun callingStackFrame() =
            Thread.currentThread().stackTrace.firstOrNull { !isLibraryFunction(it) }

        private fun isLibraryFunction(it: StackTraceElement) =
            it.className.startsWith("com.natpryce.konfig.") ||
                    it.className.startsWith("kotlin.") ||
                    it.className.startsWith("java.")
    }
}
