package issuemanage.domain

import issuemanage.domain.enums.IssuePriority
import issuemanage.domain.enums.IssueStauts
import issuemanage.domain.enums.IssueType
import jakarta.persistence.*


@Entity
@Table
class Issue (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    var userId: Long,

    @Column
    var summary: String,

    @Column
    var description: String,

    @Column
    @Enumerated(EnumType.STRING)
    var type: IssueType,

    @Column
    @Enumerated(EnumType.STRING)
    var priority: IssuePriority,

    @Column
    @Enumerated(EnumType.STRING)
    var status: IssueStauts,


        ) : BaseEntity()