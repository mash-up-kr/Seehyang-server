package mashup.spring.seehyang.service

interface JwtService<T> {
    companion object {
        private const val PREFIX_BEARER = "Bearer"
    }
    fun encode(target: T): String
    fun decode(target: String): Map<String, Any>

    fun removeTokenPrefix(type: String?, token: String): String {
        return when(type) {
            PREFIX_BEARER -> token.replace("$PREFIX_BEARER ", "")
            else -> token
        }
    }
}