package venus.utillibrary.model.base

import venus.utillibrary.function.throwIf
import venus.utillibrary.security.exceptions.JwtAuthenticationException
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import venus.utillibrary.json.LocalDateModule
import venus.utillibrary.service.getRolesFromContext
import java.lang.reflect.Field
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity<T : BaseEntity<T>> {

    companion object {
        var mapper = ObjectMapper().apply {
            registerModule(Jdk8Module())
            registerModule(KotlinModule())
            registerModule(LocalDateModule())
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    var dateAdded: LocalDateTime? = null

    @LastModifiedDate
    var dateModified: LocalDateTime? = null

    @Transient
    @JsonIgnore
    var entity: T? = null

    @PostLoad
    fun onLoad() {
        val serialized = mapper.writeValueAsString(this)
        entity = mapper.readValue(serialized, this.javaClass) as T
    }

    @PreUpdate
    fun canUpdate() {
        val fields = entity?.javaClass?.declaredFields ?: emptyArray<Field>()
        val userRoles = getRolesFromContext()

        throwIf(userRoles.isEmpty()) { JwtAuthenticationException("You have no permissions to update all object`s fields") }

        fields.asSequence()
                .filter { it.isAnnotationPresent(RoleSecured::class.java) }
                .onEach {it.isAccessible = true}
                .filter { !Objects.equals(it.get(this), it.get(entity)) }
                .flatMap { it.getAnnotation(RoleSecured::class.java).value.asSequence() }
                .distinct()
                .forEach {
                    throwIf(!userRoles.contains(it))
                    { JwtAuthenticationException("You have no permissions to update all object`s fields") }
                }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
