package userservice.utils

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import userservice.config.JWTProperties

class JWTUtilsTest {
    private val logger = KotlinLogging.logger {}

    @Test
    fun createTokenTest() {
        val jwtClaim = JWTClaim(
                userId = 1,
                email = "hihi@naver.com",
                profileUrl = "profile.jpg",
                username = "하이하이",
        )

        val properties = JWTProperties(
                issuer = "mm",
                subject = "auth",
                expiresTime = 3600,
                secret = "my-secret"
        )
        val token = JWTUtils.createToken(jwtClaim,properties)

        assertNotNull(token)

        logger.info { "token: $token" }
    }
}