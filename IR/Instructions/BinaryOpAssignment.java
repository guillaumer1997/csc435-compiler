package IR.Instructions;
import IR.*;
import Codegen.*;


public class BinaryOpAssignment extends Instruction{
    private Temp left;
    private Temp right;
    private BinaryOp op;
    private Temp assignment;

    public BinaryOpAssignment(Temp l, Temp r, BinaryOp o, Temp a){
        this.left = l;
        this.right = r;
        this.op = o;
        this.assignment = a;
    }

    public Temp getLeft(){
        return this.left;
    }

    public Temp getRight(){
        return this.right;
    }

    public Temp getAssignment(){
        return this.assignment;
    }

    public BinaryOp getOp(){
        return this.op;
    }

    public String toString(){
        if(this.op == BinaryOp.MINUS){
            return assignment.toString() + " := "+left.toString()+" "+left.getType().toShortString()+"-"+right.toString()+";";
        }
        if(this.op == BinaryOp.PLUS){
            return this.assignment.toString() + " := "+this.left.toString()+" "+this.left.getType().toShortString()+"+"+this.right.toString()+";";
        }
        if(this.op == BinaryOp.MULT){
            return assignment.toString() + " := "+left.toString()+" "+left.getType().toShortString()+"*"+right.toString()+";";
        }
        if(this.op == BinaryOp.EQUALITY){
            return assignment.toString() + " := "+left.toString()+" "+left.getType().toShortString()+"=="+right.toString()+";";
        }
        if(this.op == BinaryOp.LESSTHAN){
            return assignment.toString() + " := "+left.toString()+" "+left.getType().toShortString()+"<"+right.toString()+";";
        }
        return null;
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}