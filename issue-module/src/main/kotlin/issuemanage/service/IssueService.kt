package issuemanage.service

import issuemanage.domain.Issue
import issuemanage.domain.IssueRepository
import issuemanage.domain.enums.IssueStauts
import issuemanage.exception.NotFoundException
import issuemanage.model.IssueRequest
import issuemanage.model.IssueResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService(
        private val issueRepository: IssueRepository
) {
    @Transactional
    fun create(userId: Long, request: IssueRequest): IssueResponse {
        val issue = Issue(
                summary = request.summary,
                description = request.description,
                userId = userId,
                type = request.type,
                priority = request.priority,
                status = request.status,
        )
        return IssueResponse(issueRepository.save(issue))
    }

    @Transactional(readOnly = true)
    fun getAll(status: IssueStauts) =
            issueRepository.findAllByStatusOrderByCreatedAtDesc(status)
                    ?.map { IssueResponse(it) }

    @Transactional(readOnly = true)
    fun get(id: Long): IssueResponse {
        val issue = issueRepository.findByIdOrNull(id) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
        return IssueResponse(issue)
    }

    @Transactional
    fun edit(userId: Long, id: Long, request: IssueRequest): IssueResponse {
        val issue: Issue = issueRepository.findByIdOrNull(id) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
        return with(issue) {
            summary = request.summary
            description = request.description
            this.userId = userId
            type = request.type
            priority = request.priority
            status = request.status
            IssueResponse(issueRepository.save(this))
        }
    }

}