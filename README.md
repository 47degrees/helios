# Helios

**Helios** is a library used to transform `Json` text into a model and vice versa.
It's based on part of the [Jawn Parser](https://github.com/non/jawn) built on `Arrow`,
a Functional companion to Kotlin's Standard Library.

Learn more on [**Helios.io**]()

## Why Helios

**Helios** is one of the fastest `Json` parser libraries in `Kotlin`
with the advantage of using the `Arrow` library for functional programming.

## Adding the dependency

**Helios** uses Kotlin version `1.3.10` and `Arrow` version `0.8.1`.

To import the library on `Gradle`, add the following repository and dependencies:

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

## Running Benchmarks

To run the benchmarks for comparing Helios with other Json libraries, execute the following command:

```bash
./gradlew :helios-benchmarks: executeBenchmarks
```

To run the benchmarks with Helios' performance, execute the following command:

```bash
./gradlew :helios-benchmarks: executeHeliosBenchmark
```

## Running Microsite

To run the **Helios** microsite locally, you need to execute the following command:

```bash
bundle install --gemfile ./helios-docs/Gemfile --path vendor/bundle
```

Next, you'll be able to run the following command:

```bash
BUNDLE_GEMFILE=helios-docs/Gemfile bundle exec jekyll serve -s helios-docs/build/site/
```
