package helios.core

sealed class DecodingError
data class StringDecodingError(val value: Json) : DecodingError()
data class BooleanDecodingError(val value: Json) : DecodingError()
data class NumberDecodingError(val value: Json) : DecodingError()
data class ArrayDecodingError(val value: Json) : DecodingError()
data class ObjectDecodingError(val value: Json) : DecodingError()
data class KeyNotFound(val name: String) : DecodingError()
data class EnumValueNotFound(val value: Json) : DecodingError()