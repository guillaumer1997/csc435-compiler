package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FloatLiteral extends Literal {
    private float value;

    public FloatLiteral(float value) {
        this.value = value;
    }

    public float getValue() {
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
