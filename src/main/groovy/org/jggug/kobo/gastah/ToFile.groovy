package org.jggug.kobo.gastah

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import java.lang.annotation.*

@GroovyASTTransformationClass("org.jggug.kobo.gastah.ToFileASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ToFile {

    String value()
}
