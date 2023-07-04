package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class CharacterLiteral extends Literal {
    private char value;

    public CharacterLiteral(char value) {
        this.value = value;
    }

    public char getValue() {
        return this.value;
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