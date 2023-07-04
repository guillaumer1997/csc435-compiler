package IR.Instructions;
import IR.*;
import Codegen.*;


public class ReturnAssignment extends Instruction{
    private Temp op;

    public ReturnAssignment(){
        this.op = null;
    }

    public ReturnAssignment(Temp op){
        this.op =op;
    }

    public String toString(){
        if(op==null){
            return "RETURN;";
        }
        else{
            return "RETURN "+this.op.toString()+";";
        }
    }

    public Temp getAssignment(){
        return this.op;
    }


    public  void accept(CodegenVisitor v){
        v.visit(this);
    }
}