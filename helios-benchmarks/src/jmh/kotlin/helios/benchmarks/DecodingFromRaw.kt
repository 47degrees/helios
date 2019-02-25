package helios.benchmarks

import arrow.core.flatMap
import helios.benchmarks.sample.Friends
import helios.core.Json
import kotlinx.serialization.json.JSON
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
open class DecodingFromRaw {

  @Benchmark
  fun klaxon(): Friends = klaxon.parse<Friends>(jsonString)!!

  @Benchmark
  fun kotson(): Friends = gson.fromJson(jsonString, Friends::class.java)

  @Benchmark
  fun moshi(): Friends = moshiFriends.fromJson(jsonString)!!

  @Benchmark
  fun jackson(): Friends = jacksonFriendsReader.readValue(jsonString)

  @Benchmark
  fun helios(): Friends =
    Json.parseFromString(jsonString)
      .flatMap { heliosFriendsDecoder.decode(it) }
      .fold({ throw RuntimeException(it.toString()) }, { it })

  @Benchmark
  fun kotlinx(): Friends = JSON.parse(kotlinxFriendsSerializer, jsonString)

}

