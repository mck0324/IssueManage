package issuemanage.web

import issuemanage.config.AuthUser
import issuemanage.model.IssueRequest
import issuemanage.model.IssueResponse
import issuemanage.service.IssueService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/issues")
class IssueController(
        private val issueService: IssueService
) {

    @PostMapping
    fun create(
            authUser: AuthUser,
            @RequestBody request: IssueRequest,
    ): IssueResponse {
        return issueService.create(authUser.userId,request)
    }


}