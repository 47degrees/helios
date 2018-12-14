# Helios

**Helios** is a library used to transform `Json` text into a model and vice versa. 
It's based on a part of the [Jawn Parser](https://github.com/non/jawn) built on `Arrow`, 
a Functional companion to Kotlin's Standard Library.

## Adding the dependency

**Helios** uses kotlin `1.3.10` version and `Arrow` `0.8.1` version.

To import the library on `Gradle` add the following repository and dependencies:

```groovy
repositories {
      maven { url "https://jitpack.io" }
 }
 
dependencies {
    compile "com.47deg:helios-core:0.0.1-SNAPSHOT"
    compile "com.47deg:helios-meta:0.0.1-SNAPSHOT"
    compile "com.47deg:helios-parser:0.0.1-SNAPSHOT"
}
```

## QuickStart

You can find a quick-start [here](https://github.com/47deg/helios/tree/master/helios-docs/docs/QuickStart.md).

## Why Helios

**Helios** is the one of the fastest `Json` parser library on `Kotlin` 
with the advantage of the `Arrow` functional programming

### Decoding
Library | Score | Score Error (99.9%) | Unit |
|--|--|--|--|
helios | 19032.522733 | 1511.699343 | ops/s |
jackson | 32247.369607 | 2506.006530 | ops/s |
klaxon | 571.281780 | 14.468980 | ops/s |
kotson | 29600.046472 | 632.968692  | ops/s |
moshi | 33326.678828 | 1062.384473 | ops/s |

### DecodingFromRaw
Library | Score | Score Error (99.9%) | Unit |
|--|--|--|--|
helios | 9334.929365 | 7684.760121 | ops/s |
jackson | 23789.397621 | 2854.291651 | ops/s |
klaxon | 452.653645 | 79.123214 | ops/s |
kotlinx | 38650.988483 | 2198.522152 | ops/s |
kotson | 38081.187808 | 952.164481 | ops/s |
moshi | 25571.429371 | 1157.867183 | ops/s |

### Parsing
Library | Score | Score Error (99.9%) | Unit |
|--|--|--|--|
helios | 29960.715221 | 2098.828841 | ops/s |
jackson | 42058.986030 | 3536.245777 | ops/s |
jsonIter | 33036.995781 | 515.394397 | ops/s |
kotson | 29249.670413 | 2863.133433 | ops/s |
moshi  | 19017.079668 | 1202.955223 | ops/s |
     
##Running Benchmarks

To run the benchmarks execute the following command:

`./gradlew :helios-benchmarks:jmh -PjmhInclude=helios.benchmarks.HeliosBenchmark`
