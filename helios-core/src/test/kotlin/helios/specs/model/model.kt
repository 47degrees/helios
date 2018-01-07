package helios.specs.model

import arrow.core.Option
import helios.meta.json

@json
data class Person(
        val name: String,
        val age: Int,
        val partner: Option<Person>,
        val preferences: List<Int>,
        val properties: Map<String, String> = sortedMapOf("a" to "b"))