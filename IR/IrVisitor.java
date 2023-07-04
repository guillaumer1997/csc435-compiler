package IR;

import Types.*;
import AST.*;

public abstract class IrVisitor {
    public abstract void setProgramName(String s);

    public abstract Temp visit(AddExpression ad);

    public abstract Temp visit(ArrayAssignmentStatement aas);

    public abstract Temp visit(ArrayReference ar);

    public abstract Temp visit(AssignmentStatement as);

    public abstract Temp visit(Block b);

    public abstract Temp visit(BooleanLiteral bl);

    public abstract Temp visit(CharacterLiteral cl);

    public abstract Temp visit(EmptyStatement es);

    public abstract Temp visit(EqualityExpression ee);

    public abstract Temp visit(ExpressionList el);

    public abstract Temp visit(ExpressionStatement es);

    public abstract Temp visit(FloatLiteral fl);

    public abstract Temp visit(FormalParameter fp);

    public abstract Temp visit(FormalParameterList fpl);

    public abstract Temp visit(Function f);

    public abstract Temp visit(FunctionBody fb);

    public abstract Temp visit(FunctionCall fc);

    public abstract Temp visit(FunctionDeclaration fd);

    public abstract Temp visit(Identifier id);

    public abstract Temp visit(IfStatement is);

    public abstract Temp visit(IntegerLiteral il);

    public abstract Temp visit(LessThanExpression lte);

    public abstract Temp visit(MultExpression me);

    public abstract Temp visit(ParenExpression pe);

    public abstract Temp visit(PrintLnStatement pls);

    public abstract Temp visit(PrintStatement ps);

    public abstract Temp visit(Program p);

    public abstract Temp visit(ReturnStatement rs);

    public abstract Temp visit(StringLiteral sl);

    public abstract Temp visit(SubstractExpression se);

    public abstract Temp visit(VariableDeclaration vd);

    public abstract Temp visit(VariableDeclarationList vdl);

    public abstract Temp visit(WhileStatement ws);

    public abstract Temp visit(ArrayType at);

    public abstract Temp visit(BooleanType bt);

    public abstract Temp visit(CharType ct);

    public abstract Temp visit(TypeNode tn);

    public abstract Temp visit(FloatType ft);

    public abstract Temp visit(StringType st);

    public abstract Temp visit(IntegerType it);

    public abstract Temp visit(VoidType vt);

    public abstract Temp visit(StatementList sl);

    public abstract ProgramIR getProgram();

}
