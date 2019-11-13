package venus.searchcore.validation

import venus.utillibrary.validation.UniqueValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [UniqueValidator::class])
@MustBeDocumented
annotation class Unique(val fields: Array<String> = [],
                        val fieldsBunch: Array<String> = [],
                        val message: String = "already exists",
                        val bunchMessage: String = "already exists",
                        val errorFields: Array<String> = [],
                        val groups: Array<KClass<*>> = [],
                        val payload: Array<KClass<out Payload>> = [])
