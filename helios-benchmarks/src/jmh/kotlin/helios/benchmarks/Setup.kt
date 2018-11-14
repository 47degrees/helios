package helios.benchmarks

import com.beust.klaxon.Parser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.squareup.moshi.*
import helios.benchmarks.sample.sampleJson

val jsonString: String = sampleJson

val klaxon: Parser = Parser()

val gson = Gson()

val moshi: JsonAdapter<Map<String, Any>> = Moshi.Builder().build()
  .adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

val jackson: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

