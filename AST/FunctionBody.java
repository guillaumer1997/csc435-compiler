package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FunctionBody extends ASTNode {
    private StatementList statements;
    private VariableDeclarationList variables;

    public FunctionBody() {
        this.statements = new StatementList();
        this.variables = new VariableDeclarationList();
    }

    public void setStatementList(StatementList s) {
        this.statements = s;
    }

    public void setVarDeclList(VariableDeclarationList v) {
        this.variables = v;
    }

    public StatementList getStatements() {
        return this.statements;
    }

    public VariableDeclarationList getVarDeclList() {
        return this.variables;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) throws SemanticException {
        return v.visit(this);
    }

    public Temp accept(IrVisitor v) {
        return v.visit(this);
    }
}