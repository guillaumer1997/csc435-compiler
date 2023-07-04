package IR.Instructions;
import IR.*;
import Codegen.*;


public class OperandAssignment extends Instruction{
    private Temp assignment;
    private Temp operand;

    public OperandAssignment(Temp assignment, Temp operand){
        this.assignment = assignment;
        this.operand = operand;
    }

    public String toString(){
        return this.assignment.toString()+" := "+this.operand.toString()+";";
    }

    public Temp getLeft(){
        return this.assignment;
    }

    public Temp getRight(){
        return this.operand;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}