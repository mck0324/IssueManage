package userservice.domain.service

import org.springframework.stereotype.Service
import userservice.config.JWTProperties
import userservice.domain.entity.User
import userservice.domain.model.SignInRequest
import userservice.domain.model.SignInResponse
import userservice.domain.model.SignUpRequest
import userservice.domain.repository.UserRepository
import userservice.exception.PasswordNotMatchedException
import userservice.exception.UserExistsException
import userservice.exception.UserNotFoundException
import userservice.utils.BCryptUtils
import userservice.utils.JWTClaim
import userservice.utils.JWTUtils

@Service
class UserService(
        private val userRepository: UserRepository,
        private val jwtProperties: JWTProperties,
) {
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

            SignInResponse(
                    email = email,
                    username = username,
                    token = token
            )
        }
    }
}