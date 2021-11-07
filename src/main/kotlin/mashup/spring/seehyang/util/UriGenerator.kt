package mashup.spring.seehyang.util

class UriGenerator {
    companion object {
        fun create(uri: String, queryString: Map<String, String>): String {
            var query = ""
            for((key, value) in queryString) {
                query += "&${key}=${value}"
            }
            return "${uri}?${query.substring(1)}"
        }
    }
}