package org.jggug.kobo.gastah

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.transform.*
import org.objectweb.asm.Opcodes
import static org.objectweb.asm.Opcodes.*

abstract class AbstractASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode targetClass = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        targetClass.addMethod(new AstBuilder().buildFromSpec {
            method('printx', Opcodes.ACC_PUBLIC, Void.class) {
                parameters {
                    parameter 'arg': Object.class
                }
                exceptions {}
                block {
                    expression.addAll(printxAst)
                }
                annotations {}
            }
        }[0])
    }

}
