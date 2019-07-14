package helios.sample.retrofit

import helios.json

@json
data class Response(
  val slideshow: Slideshow
) {
  companion object
}

@json
data class Slideshow(
  val author: String,
  val date: String,
  val slides: List<Slide>,
  val title: String
) {
  companion object
}

@json
data class Slide(
  // https://github.com/47deg/helios/issues/93
//  val items: Option<List<String>>,
  val title: String,
  val type: String
) {
  companion object
}