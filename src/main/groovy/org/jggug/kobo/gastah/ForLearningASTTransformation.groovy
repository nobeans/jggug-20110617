package org.jggug.kobo.gastah

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.builder.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.transform.*
import org.objectweb.asm.Opcodes
import static org.objectweb.asm.Opcodes.*

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ForLearningASTTransformation extends ClassCodeVisitorSupport implements ASTTransformation {

    SourceUnit sourceUnit   // forced to override by super class

    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode target = (AnnotatedNode) nodes[1]
        AnnotationNode includeAnnotation = (AnnotationNode) nodes[0]

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
        def statements = additionNode.statements // 全て追加する場合(ReturnBlock含む)
        // 最後のReturnBlockは使わない場合
        if (statements.size() < 2) return
        statements = statements[0..-2]

        //def additionVariable = new AstBuilder().buildFromSpec({
        //    declaration {
        //       variable "xprintln"
        //       token "="
        //       {-> println "FOOOOOOOOOOOOOO!" }()
        //   }
        //})

        // ブロック先頭への追加
        source.getAST().getStatementBlock().getStatements().addAll(0, statements)

        // ブロック末尾への追加
        //source.getAST().getStatementBlock().getStatements().addAll(statements)

        // ブロック内の完全リプレース
        //source.getAST().getStatementBlock().getStatements().clear()
        //source.getAST().getStatementBlock().getStatements().addAll(0, statements)
    }

}

