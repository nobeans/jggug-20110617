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

@GroovyASTTransformationClass("org.jggug.kobo.gastah.CodeCompleteASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD, ElementType.LOCAL_VARIABLE])
public @interface CodeComplete {
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class CodeCompleteASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        def astStatements = source.getAST().statementBlock.statements
        source.getAST().statementBlock.statements = astStatements.collect { stmt ->
            if (stmt in ExpressionStatement && stmt.expression.is(target)) {
                return resolveStatement(target) ?: stmt
            }
            return stmt
        }
    }

    private resolveStatement(DeclarationExpression target) {
        def variableName = target.variableExpression.name
        if ("helloworld".startsWith(variableName.toLowerCase())) {
            return suppresseReturnStatement(new AstBuilder().buildFromCode({ println("Hello, World!") })[0])
        }
        return target
    }

    private suppresseReturnStatement(BlockStatement block) {
        if (block.statements.last() in ReturnStatement) {
            def replacement = new ExpressionStatement(block.statements.last().expression)
            block.statements.putAt(block.statements.size() - 1, replacement)
        }
        return block
    }
}
