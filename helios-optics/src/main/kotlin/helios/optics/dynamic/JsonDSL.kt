package helios.optics.dynamic

import helios.optics.JsonPath

/**
 * Dynamically select a path.
 *
 * example:
 * {
 *  "person": {
 *    "id": 12345,
 *    "name": "John Doe",
 *    "phones": {
 *      "home": "800-123-4567",
 *      "mobile": "877-123-1234"
 *    }
 *  }
 * }
 *
 *To select home phone: dynamic("person.phones.home")
 *
 * @param path dot notation path
 */
fun JsonPath.dynamic(path: String): JsonDynamicPath = path.split(".")
        .fold(JsonDynamicPath(dynamicJson.choice(json) compose rightJson)) { jsPath, str ->
            jsPath.select(str)
        }