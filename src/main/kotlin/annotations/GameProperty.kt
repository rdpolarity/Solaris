package annotations

import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class GameProperty {
}