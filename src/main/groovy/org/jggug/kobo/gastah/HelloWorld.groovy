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

@GroovyASTTransformationClass("org.jggug.kobo.gastah.HelloWorldASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
public @interface HelloWorld {

    String value()
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class HelloWorldASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        def additionNode = new AstBuilder().buildFromCode({
            println "Hello, World!"
        })[0]
        def statements = additionNode.statements

        // ブロック内の完全リプレース
        source.getAST().getStatementBlock().getStatements().clear()
        source.getAST().getStatementBlock().getStatements().addAll(0, statements)
    }
}
