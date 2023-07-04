package AST;

import Types.*;

public abstract class Visitor {
    public abstract void visit(AddExpression ad);

    public abstract void visit(ArrayAssignmentStatement aas);

    public abstract void visit(ArrayReference ar);

    public abstract void visit(AssignmentStatement as);

    public abstract void visit(Block b);

    public abstract void visit(BooleanLiteral bl);

    public abstract void visit(CharacterLiteral cl);

    public abstract void visit(EmptyStatement es);

    public abstract void visit(EqualityExpression ee);

    public abstract void visit(ExpressionList el);

    public abstract void visit(ExpressionStatement es);

    public abstract void visit(FloatLiteral fl);

    public abstract void visit(FormalParameter fp);

    public abstract void visit(FormalParameterList fpl);

    public abstract void visit(Function f);

    public abstract void visit(FunctionBody fb);

    public abstract void visit(FunctionCall fc);

    public abstract void visit(FunctionDeclaration fd);

    public abstract void visit(Identifier id);

    public abstract void visit(IfStatement is);

    public abstract void visit(IntegerLiteral il);

    public abstract void visit(LessThanExpression lte);

    public abstract void visit(MultExpression me);

    public abstract void visit(ParenExpression pe);

    public abstract void visit(PrintLnStatement pls);

    public abstract void visit(PrintStatement ps);

    public abstract void visit(Program p);

    public abstract void visit(ReturnStatement rs);

    public abstract void visit(StringLiteral sl);

    public abstract void visit(SubstractExpression se);

    public abstract void visit(TypeNode tn);

    public abstract void visit(VariableDeclaration vd);

    public abstract void visit(VariableDeclarationList vdl);

    public abstract void visit(WhileStatement ws);

    public abstract void visit(ArrayType at);

    public abstract void visit(BooleanType bt);

    public abstract void visit(CharType ct);

    public abstract void visit(FloatType ft);

    public abstract void visit(StringType st);

    public abstract void visit(IntegerType it);

    public abstract void visit(VoidType vt);

    public abstract void visit(StatementList sl);

}