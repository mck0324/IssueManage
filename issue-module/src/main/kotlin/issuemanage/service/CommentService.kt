package issuemanage.service

import issuemanage.domain.Comment
import issuemanage.domain.CommentRepository
import issuemanage.domain.IssueRepository
import issuemanage.exception.NotFoundException
import issuemanage.model.CommentRequest
import issuemanage.model.CommentResponse
import issuemanage.model.toResponse
import jakarta.persistence.Id
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
        private val commentRepository: CommentRepository,
        private val issueRepository: IssueRepository,
) {

    @Transactional
    fun create(issueId: Long, userId: Long, username: String, request: CommentRequest) : CommentResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다")

        val comment = Comment(
                issue = issue,
                userId = userId,
                username = username,
                body = request.body,
        )
        issue.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun edit(id: Long, userId: Long, request: CommentRequest): CommentResponse? {
        return commentRepository.findByIdAndUserId(id, userId)?.run {
            body = request.body
            commentRepository.save(this).toResponse()
        }
    }
}