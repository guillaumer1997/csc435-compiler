package IR;

import IR.Instructions.*;
import Codegen.*;

public class Label extends Instruction {
    private int index;

    public Label(int index) {
        this.index = index;
    }

    public int getNumber() {
        return this.index;
    }

    public String toString() {
        return "L" + this.index + ":;";
    }

    public void accept(CodegenVisitor v) {
        v.visit(this);
    }
}