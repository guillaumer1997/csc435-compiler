package IR.Instructions;
import IR.*;
import Codegen.*;

public class ArrayIndexAssignment extends Instruction{
    private Temp assignment;
    private Temp operand;
    private Temp index;

    public ArrayIndexAssignment(Temp a, Temp o, Temp i){
        this.assignment = a;
        this.operand = o;
        this.index = i;
    }

    public String toString(){
        return this.assignment.toString()+"["+this.index.toString()+"]"+":="+this.operand.toString()+";";
    }

    public Temp getLeft(){
        return this.assignment;
    }

    public Temp getIndex(){
        return this.index;
    }

    public Temp getRight(){
        return this.operand;
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}