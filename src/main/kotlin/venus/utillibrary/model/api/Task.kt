package venus.utillibrary.model.api

import org.springframework.data.annotation.CreatedBy
import venus.utillibrary.model.base.BaseEntity
import venus.utillibrary.model.base.Role
import venus.utillibrary.model.base.RoleSecured
import venus.utillibrary.model.base.User
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Task")
@AttributeOverrides(
        AttributeOverride(name = "id", column = Column(name = "Tsk_Id")),
        AttributeOverride(name = "dateAdded", column = Column(name = "Tsk_Date_Added", columnDefinition = "TIMESTAMP")),
        AttributeOverride(name = "dateModified", column = Column(name = "Tsk_Date_Modified", columnDefinition = "TIMESTAMP")))
class Task : BaseEntity<Task>() {

    @Column(name = "Tsk_Title", nullable = false)
    var title: String = ""

    @Column(name = "Tsk_Comment", columnDefinition = "VARCHAR(255) default ''")
    var comment: String = ""

    @Column(name = "Tsk_Complete", columnDefinition = "TIMESTAMP")
    var dateComplete: LocalDateTime? = null

    @Column(name = "Tsk_Due_Date", columnDefinition = "DATE")
    var dueDate: LocalDate? = null

    @ManyToOne
    @NotNull
    @CreatedBy
    @JoinColumn(name = "Tsk_Usr_Id", nullable = false)
    @RoleSecured(Role.ROLE_ADMIN)
    var userAdded: User? = null

    @Enumerated
    @Column(name = "Tsk_Type", nullable = false)
    var type: TaskType = TaskType.NORMAL

    @Enumerated
    @Column(name = "Tsk_Status", nullable = false)
    var status: TaskStatus = TaskStatus.ACTIVE
}
