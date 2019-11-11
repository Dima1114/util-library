package venus.utillibrary.projection

import venus.utillibrary.entity.TestEntity
import api.entity.TestEntity2
import org.springframework.data.rest.core.config.Projection
import java.time.LocalDate

@Projection(types = [TestEntity::class], name = "test")
interface TestProjection{

    fun getId(): Long?
    fun getString(): String?
    fun getDate(): LocalDate?
    fun getFloat(): Float?
    fun getChild(): TestEntity2?
}