package helios.benchmarks

import com.beust.klaxon.JsonObject
import com.fasterxml.jackson.databind.JsonNode
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.JsonElement
import com.jsoniter.JsonIterator
import helios.core.Json
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
open class Parsing {

  @Benchmark
  fun klaxon(): JsonObject = klaxonParser.parse(StringBuilder(jsonString)) as JsonObject

  @Benchmark
  fun kotson(): JsonElement = gson.fromJson(jsonString)

  @Benchmark
  fun moshi(): Map<String, Any?> = moshi.fromJson(jsonString)!!

  @Benchmark
  fun jackson(): JsonNode = jackson.readTree(jsonString)

  @Benchmark
  fun helios(): Json = Json.parseUnsafe(jsonString)

  @Benchmark
  fun jsonIter(): Map<Any?, Any?> = JsonIterator.parse(jsonString).read() as Map<Any?, Any?>

}

