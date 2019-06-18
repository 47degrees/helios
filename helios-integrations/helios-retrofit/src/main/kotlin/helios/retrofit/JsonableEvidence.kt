package helios.retrofit

import arrow.core.Tuple3
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

typealias JsonableEvidence<T> = Tuple3<Class<T>, Encoder<T>, Decoder<T>>