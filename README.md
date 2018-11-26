# Helios

Json library based on a port of the [Jawn Parser](https://github.com/non/jawn) built on Arrow.

## How to use it

`Helios` uses kotlin `1.3.10` version and `Arrow` `0.8.1` version.

To import the library on Gradle add to the dependencies:

```
dependencies {
    compile "com.47deg:helios-core:0.0.1-SNAPSHOT"
    compile "com.47deg:helios-meta:0.0.1-SNAPSHOT"
    compile "com.47deg:helios-parser:0.0.1-SNAPSHOT"
}
```

Once it's imported, we just need to define our module on this way 

```kotlin
@json
data class Person(val name: String, val age: Int) {
  companion object
}
```

The `@json` annotation will provide the decoder and encoder for that data class, so we are able to

### Decode

```kotlin
val jsonStr = 
"""{
     "name": "Simon",
     "age": 30
   }"""
    
val jsonFromString : Json = Json.parseUnsafe(companyJsonString)

val personOrError: Either<DecodingError, Person> = Person.decoder().decode(jsonFromString)

personOrError.fold({
  println("Something went wrong during decoding: $it")
}, {
  println("Successfully decode the json: $it")
})
```

### Encode

```kotlin
val person = Person("Raul", 34)

val jsonFromPerson = with(Person.encoder()) {
  person.encode()
}

println(streetJson.toJsonString())
```
