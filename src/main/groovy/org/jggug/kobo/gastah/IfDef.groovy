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

@GroovyASTTransformationClass("org.jggug.kobo.gastah.IfDefASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
public @interface IfDef {

    String value() // envname
}

@GroovyASTTransformationClass("org.jggug.kobo.gastah.IfDefASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
public @interface IfNotDef {

    String value() // envname
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class IfDefASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        // @IfDefなら環境変数が存在したら、取り除かない(return)
        // @IfNotDefなら環境変数が存在しなかったら、取り除かない(return)
        def isIfDef = includeAnnotation.classNode.typeClass in IfDef
        def envname = includeAnnotation.members.value.text
        def existsEnv = System.getenv(envname)
        if ( isIfDef &&  existsEnv) return
        if (!isIfDef && !existsEnv) return

        // コードから取り除く
        def astStatements = source.getAST().statementBlock.statements
        source.getAST().statementBlock.statements = astStatements.findAll { stmt ->
            return !(stmt in ExpressionStatement && stmt.expression.is(target))
        }
    }
}

