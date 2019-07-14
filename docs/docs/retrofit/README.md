---
layout: docs
title: Retrofit integration
permalink: /docs/retrofit/
---

# Retrofit integration

Helios contains retrofit integration in module `helios-integration-retrofit`.

## Usage

Integration module provides implementation of Retrofit `Converter.Factory` called `HeliosConverterFactory`. Class is constructed using `create` function that accepts triples of class, encoder and decoder.

## Example

```kotlin
@json
data class Person(
  val name: String,
  val age: Int
)

fun getRetrofit(): Retrofit {
  
  return new Retrofit.Builder()
    .addConverterFactory(HeliosConverterFactory.create(
      JsonableEvidence(Person::class, Person.encoder(), Person.decoder())
    ))
    .baseUrl("https://api.github.com/")
    .build()

}
```

There is also example project available in https://github.com/47deg/helios/tree/master/helios-sample-retrofit