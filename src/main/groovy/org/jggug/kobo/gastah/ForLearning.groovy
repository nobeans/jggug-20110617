package org.jggug.kobo.gastah

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import java.lang.annotation.*

@GroovyASTTransformationClass("org.jggug.kobo.gastah.ForLearningASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForLearning {

    String value()
}
