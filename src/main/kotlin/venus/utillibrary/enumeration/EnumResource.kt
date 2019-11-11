package venus.utillibrary.enumeration

import venus.utillibrary.model.base.Role
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Inherited
@MustBeDocumented
annotation class EnumResource(vararg val secured: Role = [])
