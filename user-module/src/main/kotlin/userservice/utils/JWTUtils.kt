package userservice.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import userservice.config.JWTProperties
import java.util.Date

object JWTUtils {

    fun createToken(claim: JWTClaim, properties: JWTProperties) =
        JWT.create()
                .withIssuer(properties.issuer)
                .withSubject(properties.subject)
                .withIssuedAt(Date())
                .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
                .withClaim("userId",claim.userId)
                .withClaim("email",claim.email)
                .withClaim("profileUrl",claim.profileUrl)
                .withClaim("username",claim.username)
                .sign(Algorithm.HMAC256(properties.secret))
}

data class JWTClaim(
        val userId: String,
        val email: String,
        val profileUrl: String,
        val username: String,
)