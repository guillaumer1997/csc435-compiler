package TypeCheck;

import Types.*;
import AST.*;

public abstract class TypeVisitor {
    public abstract Type visit(AddExpression ad) throws SemanticException;

    public abstract Type visit(ArrayAssignmentStatement aas) throws SemanticException;

    public abstract Type visit(ArrayReference ar) throws SemanticException;

    public abstract Type visit(AssignmentStatement as) throws SemanticException;

    public abstract Type visit(Block b) throws SemanticException;

    public abstract Type visit(BooleanLiteral bl) throws SemanticException;

    public abstract Type visit(CharacterLiteral cl) throws SemanticException;

    public abstract Type visit(EmptyStatement es) throws SemanticException;

    public abstract Type visit(EqualityExpression ee) throws SemanticException;

    public abstract Type visit(ExpressionList el) throws SemanticException;

    public abstract Type visit(ExpressionStatement es) throws SemanticException;

    public abstract Type visit(FloatLiteral fl) throws SemanticException;

    public abstract Type visit(FormalParameter fp) throws SemanticException;

    public abstract Type visit(FormalParameterList fpl) throws SemanticException;

    public abstract Type visit(Function f) throws SemanticException;

    public abstract Type visit(FunctionBody fb) throws SemanticException;

    public abstract Type visit(FunctionCall fc) throws SemanticException;

    public abstract Type visit(FunctionDeclaration fd) throws SemanticException;

    public abstract Type visit(Identifier id) throws SemanticException;

    public abstract Type visit(IfStatement is) throws SemanticException;

    public abstract Type visit(IntegerLiteral il) throws SemanticException;

    public abstract Type visit(LessThanExpression lte) throws SemanticException;

    public abstract Type visit(MultExpression me) throws SemanticException;

    public abstract Type visit(ParenExpression pe) throws SemanticException;

    public abstract Type visit(PrintLnStatement pls) throws SemanticException;

    public abstract Type visit(PrintStatement ps) throws SemanticException;

    public abstract Type visit(Program p) throws SemanticException;

    public abstract Type visit(ReturnStatement rs) throws SemanticException;

    public abstract Type visit(StringLiteral sl) throws SemanticException;

    public abstract Type visit(SubstractExpression se) throws SemanticException;

    public abstract Type visit(TypeNode tn) throws SemanticException;

    public abstract Type visit(VariableDeclaration vd) throws SemanticException;

    public abstract Type visit(VariableDeclarationList vdl) throws SemanticException;

    public abstract Type visit(WhileStatement ws) throws SemanticException;

    public abstract Type visit(ArrayType at) throws SemanticException;

    public abstract Type visit(BooleanType bt) throws SemanticException;

    public abstract Type visit(CharType ct) throws SemanticException;

    public abstract Type visit(FloatType ft) throws SemanticException;

    public abstract Type visit(StringType st) throws SemanticException;

    public abstract Type visit(IntegerType it) throws SemanticException;

    public abstract Type visit(VoidType vt) throws SemanticException;

    public abstract Type visit(StatementList sl) throws SemanticException;

}