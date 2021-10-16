package mashup.spring.seehyang.service

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.RuntimeException

@Service
class UserJwtService(
    @Value("\${jwt.token-issuer}")
    private val tokenIssuer: String? = null,
    @Value("\${jwt.token-secret-key}")
    private val jwtSecretKey: String? = null,
    @Value("\${jwt.token-type}")
    private val jwtTokenType: String? = null,
): JwtService<Long>{

    override fun encode(target: Long): String {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(tokenIssuer)
            .claim("id", target)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    override fun decode(target: String): Long {
        val decoded: Map<String, Any> = try {
            Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(removeTokenPrefix(jwtTokenType, target)).body
        } catch (err: RuntimeException) {
            throw RuntimeException(err.message)
        }
        return decoded["id"].toString().toLong()
    }
}