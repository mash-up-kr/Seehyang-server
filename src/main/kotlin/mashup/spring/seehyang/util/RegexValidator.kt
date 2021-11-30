package mashup.spring.seehyang.util
import java.util.regex.Pattern

/**
 * Email Validator
 * Email RFC 5322 Official Standard
 */
val EMAIL_REGEX_PATTERN: String = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

fun isValidEmailFormat(email: String): Boolean {
    val isEmailFormat = validator(EMAIL_REGEX_PATTERN, email)
    return isEmailFormat
}

/**
 * English Name Validator
 * English + Space + Number
 */

val ENG_SPACE_NUM_PATTERN = "^[a-zA-Z0-9\\s]+\$"

fun isOnlyEngSpaceNumber(target: String):Boolean{
    return validator(ENG_SPACE_NUM_PATTERN, target)
}

val KOREAN_SPACE_NUM_PATTERN = "^[0-9가-힣\\s]+\$"

fun isOnlyKoreanSpaceNumber(target: String):Boolean{
    return validator(KOREAN_SPACE_NUM_PATTERN, target)
}





private fun validator(pattern:String, target:String):Boolean{
    return Pattern.matches(pattern, target)
}
