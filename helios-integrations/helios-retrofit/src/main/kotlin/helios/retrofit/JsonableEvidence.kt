package helios.retrofit

import arrow.core.Tuple3
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import kotlin.reflect.KClass

typealias JsonableEvidence<T> = Tuple3<KClass<T>, Encoder<T>, Decoder<T>>