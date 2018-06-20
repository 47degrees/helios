package helios.benchmarks

import com.beust.klaxon.JsonObject
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectReader
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.JsonElement
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import helios.benchmarks.sample.Friends
import helios.benchmarks.sample.decoder
import helios.core.Json
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
open class Decoding {

    @Benchmark
    fun klaxon(): Friends = Parsed.Klaxon.parseFromJsonObject<Friends>(Parsed.klaxonJson)!!

    @Benchmark
    fun kotson(): Friends = gson.fromJson(Parsed.kotsonJson, Friends::class.java)

    @Benchmark
    fun moshi(): Friends = Parsed.moshiFriends.fromJsonValue(Parsed.moshiObject)!!

    @Benchmark
    fun jackson(): Friends = Parsed.jacksonFriendsReader.readValue(Parsed.jacksonJson)

    @Benchmark
    fun helios(): Friends = Parsed.heliosFriendsDecoder.decode(Parsed.heliosJson).fold({
        throw RuntimeException(it.toString())
    }, { it })

}

object Parsed {

    val Klaxon = com.beust.klaxon.Klaxon()

    val moshiFriends: JsonAdapter<Friends> = Moshi.Builder().build().adapter(Friends::class.java)

    val klaxonJson: JsonObject = klaxon.parse(StringBuilder(jsonString)) as JsonObject

    val kotsonJson: JsonElement = gson.fromJson(jsonString)

    val moshiObject: Map<String, Any?> = moshi.fromJson(jsonString)!!

    val jacksonFriendsReader: ObjectReader = jackson.readerFor(Friends::class.java)

    val jacksonJson: JsonNode = jackson.readTree(jsonString)

    val heliosFriendsDecoder = Friends.decoder()

    val heliosJson: Json = Json.parseUnsafe(jsonString)

}

