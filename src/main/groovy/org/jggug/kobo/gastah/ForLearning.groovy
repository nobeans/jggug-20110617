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

@GroovyASTTransformationClass("org.jggug.kobo.gastah.ForLearningASTTransformation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForLearning {

    String value()
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ForLearningASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

        dump(nodes, source)

        //def additionNode = new AstBuilder().buildFromCode({
        //    // 明示的なreturnが内場合には、暗黙のreturn(ReturnBlock)になってしまう
        //    // →既存コードにステートメント追加しても後続の処理が継続しない
        //    println "XXX:"
        //})[0]

        def additionNode = new AstBuilder().buildFromCode({
            println this
            println this.class
            println "XXX!"
            println "YYY!"
            println "ZZZ!"
            return null // 明示的returnがあれば、それ以前のステートメントがReturnBlockになることはない
        })[0]
        def additionStatements = additionNode.statements
        if (additionStatements.size() >= 2) { // 最後のReturnBlockは使わない場合
            additionStatements = additionStatements[0..-2]
        }
        def additionStatement = new BlockStatement(additionStatements, new VariableScope())

        //def additionVariable = new AstBuilder().buildFromSpec({
        //    declaration {
        //       variable "xprintln"
        //       token "="
        //       {-> println "FOOOOOOOOOOOOOO!" }()
        //   }
        //})

        // ブロック先頭への追加
        //source.getAST().getStatementBlock().getStatements().addAll(0, additionStatement)

        // ブロック末尾への追加
        //source.getAST().getStatementBlock().getStatements().addAll(additionStatement)

        // ブロック内の完全リプレース
        //source.getAST().getStatementBlock().getStatements().clear()
        //source.getAST().getStatementBlock().getStatements().addAll(0, additionStatement)

        // アノテーションを付けた文のみ置換
        def astStatements = source.getAST().statementBlock.statements
        source.getAST().statementBlock.statements = astStatements.collect { stmt ->
            if (stmt in ExpressionStatement && stmt.expression.is(target)) {
                return additionStatement
            }
            return stmt
        }
    }

    void dump(nodes, source) {
        def target = (AnnotatedNode) nodes[1]
        def includeAnnotation = (AnnotationNode) nodes[0]
        def printWithIndent = { println " "*4 + it }
        println ">"*50
        println "Nodes: ["
        nodes.each printWithIndent
        println "]"
        println "Target(Nodes[1]): $target"
        println "Annotation(Nodes[0]): ["
        includeAnnotation.properties.each printWithIndent
        println "]"
        println "SourceUnit: ["
        source.properties.each printWithIndent
        println "]"
        println "<"*50
    }

    void dump(Statement stmt) {
        println "${stmt.class.simpleName}: ["
        stmt.properties.each printWithIndent
        println "]"
    }
}

