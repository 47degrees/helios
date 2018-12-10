package helios.benchmarks

import arrow.core.flatMap
import helios.benchmarks.sample.*
import helios.core.Json
import kotlinx.serialization.json.JSON
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10)
open class HeliosBenchmark {

  @Benchmark
  fun parsing(): Json = Json.parseUnsafe(jsonString)

  @Benchmark
  fun decoding(): Friends = heliosFriendsDecoder.decode(Parsed.heliosJson).fold({
    throw RuntimeException(it.toString())
  }, { it })

}

