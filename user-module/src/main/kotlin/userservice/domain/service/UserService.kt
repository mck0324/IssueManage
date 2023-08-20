package userservice.domain.service

import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Service
import userservice.config.JWTProperties
import userservice.domain.entity.User
import userservice.domain.model.SignInRequest
import userservice.domain.model.SignInResponse
import userservice.domain.model.SignUpRequest
import userservice.domain.repository.UserRepository
import userservice.exception.InvalidJwtTokenException
import userservice.exception.PasswordNotMatchedException
import userservice.exception.UserExistsException
import userservice.exception.UserNotFoundException
import userservice.utils.BCryptUtils
import userservice.utils.JWTClaim
import userservice.utils.JWTUtils
import java.time.Duration

@Service
class UserService(
        private val userRepository: UserRepository,
        private val jwtProperties: JWTProperties,
        private val cacheManager: CoroutinCacheManager<User>,
) {
    companion object {
        private val CACHE_TTL = Duration.ofMinutes(1)
    }
    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {
            userRepository.findByEmail(this.email)?. let {
                throw UserExistsException()
            }
            val user = User(
                    email = email,
                    password = BCryptUtils.hash(password),
                    username = username,
            )
            userRepository.save(user)
        }
    }

    suspend fun signIn(signInRequest: SignInRequest): SignInResponse {
        return with(userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
            val verified = BCryptUtils.verify(signInRequest.password, password)
            if (!verified) {
                throw PasswordNotMatchedException()
            }
            val jwtClaim = JWTClaim(
                    userId = id!!,
                    email = email,
                    profileUrl = profileUrl,
                    username = username
            )
            val token = JWTUtils.createToken(jwtClaim, jwtProperties)

            cacheManager.awaitPut(key = token, value = this, ttl = CACHE_TTL)
            SignInResponse(
                    email = email,
                    username = username,
                    token = token
            )
        }
    }

    suspend fun logout(token: String) {
        cacheManager.awaitEvict(token)
    }

    suspend fun getByToken(token: String): User {
        val decodedJWT: DecodedJWT = JWTUtils.decode(token, jwtProperties.secret, jwtProperties.issuer)

        val cachedUser = cacheManager.awaitGetOrPut(key = token, ttl = CACHE_TTL) {
            //캐시가 유효하지 않은 경우 동작
            val decodedJWT: DecodedJWT = JWTUtils.decode(token, jwtProperties.secret, jwtProperties.issuer)

            val userId: Long = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()
            get(userId)
        }
        return cachedUser
    }

    suspend fun get(userId: Long) : User {
        return  userRepository.findById(userId) ?: throw UserNotFoundException()
    }
}