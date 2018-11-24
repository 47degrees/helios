# Helios

Json library based on a port of the [Jawn Parser](https://github.com/non/jawn) built on Arrow.

## How to use it

To import the library on Gradle add to the dependencies:

```

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
