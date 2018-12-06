# Helios

Json library based on a port of the [Jawn Parser](https://github.com/non/jawn) built on Arrow.

## How to use it

`Helios` uses kotlin `1.3.10` version and `Arrow` `0.8.1` version.

To import the library on Gradle add the following repository and dependencies:

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

Once it's imported, we just need to define our module on this way 

```kotlin:ank
@json
data class Person(val name: String, val age: Int) {
  companion object
}
```

The `@json` annotation will provide the decoder and encoder for that data class, so we are able to

### Decode

```kotlin:ank
val jsonStr = 
"""{
     "name": "Simon",
     "age": 30
   }"""
    
val jsonFromString : Json = 
  Json.parseFromString(jsonStr).getOrHandle {
    println("Failed creating the Json ${it.localizedMessage}, creating an empty one")
    JsString("")
  }

val personOrError: Either<DecodingError, Person> = Person.decoder().decode(jsonFromString)

personOrError.fold({
  println("Something went wrong during decoding: $it")
}, {
  println("Successfully decode the json: $it")
})
```

### Encode

```kotlin:ank
val person = Person("Raul", 34)

val jsonFromPerson = with(Person.encoder()) {
  person.encode()
}

println(jsonFromPerson.toJsonString())
```
