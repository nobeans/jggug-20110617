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

@GroovyASTTransformationClass("org.jggug.kobo.gastah.DebugASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
public @interface Debug {

    Class value()
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class DebugASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        if (!System.getenv("DEBUG")) return

        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        def closureStmt = includeAnnotation.members.value.code

        def astStatements = source.getAST().statementBlock.statements
        source.getAST().statementBlock.statements = astStatements.collect { stmt ->
            if (stmt in ExpressionStatement && stmt.expression.is(target)) {
                return closureStmt
            }
            return stmt
        }
    }
}
