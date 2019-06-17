package helios.retrofit

import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

typealias JsonableEvidence<T> = Triple<Class<T>, Encoder<T>, Decoder<T>>