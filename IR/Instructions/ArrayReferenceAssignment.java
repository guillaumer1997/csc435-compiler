package IR.Instructions;
import IR.*;

import Codegen.*;

public class ArrayReferenceAssignment extends Instruction{
    private Temp assignment;
    private Temp operand;
    private Temp index;

    public ArrayReferenceAssignment(Temp a, Temp o, Temp i){
        this.assignment = a;
        this.operand = o;
        this.index = i;
    }

    public Temp getLeft(){
        return this.assignment;
    }

    public Temp getRight(){
        return this.operand;
    }

    public Temp getIndex(){
        return this.index;
    }



    public String toString(){
        return this.assignment.toString()+":="+this.operand.toString()+"["+this.index.toString()+"]" +";";
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}