package org.jggug.kobo.gastah

import java.lang.annotation.*
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.transform.*
import org.objectweb.asm.Opcodes
import static org.objectweb.asm.Opcodes.*

@GroovyASTTransformationClass("org.jggug.kobo.gastah.ToFileASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ToFile {

    String value()
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ToFileASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class
    final static Set<Class> visitedClass = new HashSet<Class>().asSynchronized()

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode targetClass = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        if (visitedClass.contains(targetClass)) return

        targetClass.addMethod(new AstBuilder().buildFromSpec {
            method('xprintln', Opcodes.ACC_PUBLIC, Void.class) {
                parameters {
                    parameter 'arg': Object.class
                }
                exceptions {}
                block {
                    expression.addAll(new AstBuilder().buildFromCode({
                        if (arg != null) {
                            def file = this.class.getAnnotation(org.jggug.kobo.gastah.ToFile.class).value()
                            new File(file).append(arg + System.properties["line.separator"])
                        }
                    }))
                }
                annotations {}
            }
        }[0])

        visitedClass.add(targetClass)
    }

}
