package api.entity

import venus.utillibrary.enumeration.EnumResource

@EnumResource
enum class TaskStatus {
    ACTIVE, COMPLETED, IN_BIN, OVERDUE
}