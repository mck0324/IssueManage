package issuemanage.web

import issuemanage.config.AuthUser
import issuemanage.model.CommentRequest
import issuemanage.model.CommentResponse
import issuemanage.service.CommentService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/{issueId}/comments")
class CommentController (
        private val commentService: CommentService
){
    @PostMapping
    fun create(
            authUser: AuthUser,
            @PathVariable issueId: Long,
            @RequestBody request: CommentRequest,
    ): CommentResponse {
        return commentService.create(issueId, authUser.userId, authUser.username, request)
    }
}