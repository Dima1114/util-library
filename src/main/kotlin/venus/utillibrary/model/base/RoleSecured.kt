package venus.utillibrary.model.base

@Target(
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
@MustBeDocumented
annotation class RoleSecured(vararg val value: Role = [])
