package helios.benchmarks

import com.beust.klaxon.JsonObject
import com.fasterxml.jackson.databind.JsonNode
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.JsonElement
import helios.benchmarks.sample.*
import helios.core.Json
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
open class Decoding {

  @Benchmark
  fun klaxon(): Friends = klaxon.parseFromJsonObject<Friends>(Parsed.klaxonJson)!!

  @Benchmark
  fun kotson(): Friends = gson.fromJson(Parsed.kotsonJson, Friends::class.java)

  @Benchmark
  fun moshi(): Friends = moshiFriends.fromJsonValue(Parsed.moshiObject)!!

  @Benchmark
  fun jackson(): Friends = jacksonFriendsReader.readValue(Parsed.jacksonJson)

  @Benchmark
  fun helios(): Friends = heliosFriendsDecoder.decode(Parsed.heliosJson).fold({
    throw RuntimeException(it.toString())
  }, { it })

}

object Parsed {

  val klaxonJson: JsonObject = klaxonParser.parse(StringBuilder(jsonString)) as JsonObject

  val kotsonJson: JsonElement = gson.fromJson(jsonString)

  val moshiObject: Map<String, Any?> = moshi.fromJson(jsonString)!!

  val jacksonJson: JsonNode = jackson.readTree(jsonString)

  val heliosJson: Json = Json.parseUnsafe(jsonString)

}

