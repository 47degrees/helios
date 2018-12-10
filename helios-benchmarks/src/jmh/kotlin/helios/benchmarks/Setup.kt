package helios.benchmarks

import com.beust.klaxon.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.squareup.moshi.*
import helios.benchmarks.sample.*

val jsonString: String = sampleJson

val klaxonParser: Parser = Parser.default()

val klaxon: Klaxon = com.beust.klaxon.Klaxon()

val gson = Gson()

val moshi: JsonAdapter<Map<String, Any>> = Moshi.Builder().build()
  .adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

val moshiFriends: JsonAdapter<Friends> = Moshi.Builder().build()
  .adapter(Friends::class.java)

val jackson: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

val jacksonFriendsReader: ObjectReader = jackson.readerFor(Friends::class.java)

val heliosFriendsDecoder = Friends.decoder()

val kotlinxFriendsSerializer = Friends.serializer()