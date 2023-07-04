package AST;

import java.util.*;
import Types.*;
import TypeCheck.*;
import IR.*;

public class StatementList extends ASTNode {
    public List<Statement> statements;

    public StatementList() {
        this.statements = new ArrayList<Statement>();
    }

    public void addStatement(Statement s) {
        this.statements.add(s);
    }

    public int getSize() {
        return this.statements.size();
    }

    public Statement getElement(int i) {
        return this.statements.get(i);
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