package mashup.spring.seehyang.service

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mashup.spring.seehyang.controller.api.dto.user.UserDto
import mashup.spring.seehyang.domain.entity.Image
import mashup.spring.seehyang.domain.entity.community.OAuthType
import mashup.spring.seehyang.domain.entity.perfume.Gender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.RuntimeException

@Service
class UserJwtService(
    @Value("\${jwt.token-issuer}")
    private val tokenIssuer: String? = null,
    @Value("\${jwt.token-secret-key}")
    private val jwtSecretKey: String? = null,
    @Value("\${jwt.token-type}")
    private val jwtTokenType: String? = null,
): JwtService<UserDto>{

    override fun encode(target: UserDto): String {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(tokenIssuer)
            .claim("id", target.id)
            .claim("gender", target.gender)
            .claim("age", target.age)
            .claim("nickname", target.nickname)
            .claim("email", target.email)
            .claim("oAuthType", target.oAuthType)
            .claim("profileImage", target.profileImage)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    @Transactional(readOnly = true)
    override fun decode(target: String): UserDto {
        val decoded: Map<String, Any> = try {
            Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(removeTokenPrefix(jwtTokenType, target)).body
        } catch (err: RuntimeException) {
            throw RuntimeException(err.message)
        }
        return UserDto(
            id = decoded["id"].toString().toLong(),
            gender = Gender.valueOf((decoded["gender"] ?: Gender.BOTH).toString()),
            age = decoded["age"]?.toString()?.toShort(),
            nickname = decoded["nickname"]?.toString(),
            email = decoded["email"].toString(),
            oAuthType = OAuthType.valueOf(decoded["oAuthType"].toString()),
            profileImage = decoded["profileImage"] as? Image
        )
    }
}