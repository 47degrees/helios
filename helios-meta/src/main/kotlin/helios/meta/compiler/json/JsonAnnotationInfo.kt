package helios.meta.compiler.json

import helios.json

val jsonAnnotationKClass = json::class
val jsonAnnotationClass = jsonAnnotationKClass.java
val jsonAnnotationName = "@" + jsonAnnotationClass.simpleName
