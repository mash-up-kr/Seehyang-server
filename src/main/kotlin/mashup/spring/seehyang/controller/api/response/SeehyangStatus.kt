package mashup.spring.seehyang.controller.api.response

enum class SeehyangStatus(
    val code: Int,
    val message: String
) {
    OK(2000, "OK"),

    INVALID_CURSOR_PARAMETER(4001, "You need like cursor and id cursor both."),
    ALREADY_EXIST_USER(4002, "Already exist user."),
    NOT_EXIST_OAUTH_TYPE(4003, "Not exist oauth type. ex) GOOGLE, APPLE"),
    CONTENTS_IS_EMPTY(4004, "Contents is empty or blank"),

    UNAUTHORIZED_USER(4010, "Unauthenticated user."),
    UNAUTHORIZED_INVALID_TOKEN(4011, "Invalid token."),

    NOT_FOUND_USER(4040, "The user does not exist."),
    NOT_FOUND_COMMENT(4041, "You need to write the content."),
    NOT_FOUND_STORY(4042, "The story does not exist."),
    NOT_FOUND_PERFUME(4043,"The perfume does not exist."),
    NOT_FOUND_STORYLIKE(4044,"The story like does not exist"),

    INTERNAL_SERVER_ERROR(5000, "This request cannot be proceed.");


    companion object {
        fun findByCode(code: Int) = values().first { it.code == code }
    }
}