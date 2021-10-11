package mashup.spring.seehyang.service

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mashup.spring.seehyang.domain.entity.community.User
import mashup.spring.seehyang.repository.user.UserRepository
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

    private val userRepository: UserRepository,
) : JwtService<User>{

    override fun encode(target: User): String {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(tokenIssuer)
            .claim("id", target.id)
            .claim("gender", target.gender)
            .claim("age", target.age)
            .claim("nickname", target.nickname)
            .claim("email", target.email)
            .claim("oAuthType", target.oAuthType)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    @Transactional(readOnly = true)
    override fun decode(target: String): User? {
        val decoded: Map<String, Any> = try {
            Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(removeTokenPrefix(jwtTokenType, target)).body
        } catch (err: RuntimeException) {
            throw RuntimeException(err.message)
        }
        return userRepository.findById(decoded["id"].toString().toLong())
            .orElse(null)
    }
}