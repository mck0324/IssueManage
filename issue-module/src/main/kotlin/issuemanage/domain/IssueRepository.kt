package issuemanage.domain

import issuemanage.domain.enums.IssueStauts
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository : JpaRepository<Issue, Long>{
    fun findAllByStatusOrderByCreatedAtDesc(status: IssueStauts): List<Issue>?
}