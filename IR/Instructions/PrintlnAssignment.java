package IR.Instructions;
import IR.*;
import Types.*;
import Codegen.*;


public class PrintlnAssignment extends Instruction{
    private Temp op;
    private Type type;

    public PrintlnAssignment(Temp op, Type type){
        this.op = op;
        this.type = type;
    }

    public String toString(){
        if(this.type instanceof StringType){
            return "PRINTLNU "+this.op.toString()+";";
        }
        else{
            return "PRINTLN"+this.type.toShortString()+" "+this.op.toString()+";";
        }
    }

    public Temp getTemp(){
        return this.op;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}