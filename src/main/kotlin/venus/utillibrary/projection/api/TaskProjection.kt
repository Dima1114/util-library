package venus.utillibrary.projection.api

import api.entity.TaskStatus
import org.springframework.data.rest.core.config.Projection
import venus.utillibrary.model.api.Task
import venus.utillibrary.model.api.TaskType
import venus.utillibrary.model.base.User
import java.time.LocalDate
import java.time.LocalDateTime

@Projection(name = "info", types = [Task::class])
interface TaskProjection : BaseProjection {

    val title: String

    val comment: String

    val dateComplete: LocalDateTime?

    val dueDate: LocalDate?

    val userAdded: User?

    val type: TaskType

    val status: TaskStatus
}
