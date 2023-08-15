package issuemanage.domain.enums

enum class IssueStauts {
     TODO, IN_PROGRESS, RESOLVED;

    companion object {
        operator fun invoke(stauts: String) = valueOf(stauts.uppercase())
    }

}
