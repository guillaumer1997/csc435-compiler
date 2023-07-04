package IR.Instructions;
import IR.*;
import Codegen.*;


public class UnaryOpAssignment extends Instruction{
    private Temp operand;
    private Temp assignment;
    private UnaryOp op;


    public UnaryOpAssignment(Temp o, Temp a, UnaryOp op){
        this.operand = o;
        this.assignment = a;
        this.op = op;
    }

    public Temp getLeft(){
        return this.assignment;
    }

    public UnaryOp getOp(){
        return this.op;
    }

    public Temp getRight(){
        return this.operand;
    }

    public String toString(){
        if(this.op == UnaryOp.NEGATION){
            return this.assignment.toString()+":="+this.assignment.getType().toShortString()+" - "+this.operand.toString()+";";
        }
        if(this.op == UnaryOp.INVERSION){
            return this.assignment.toString()+":="+this.assignment.getType().toShortString()+" ! "+this.operand.toString()+";";
        }

        return null;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}