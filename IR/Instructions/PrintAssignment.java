package IR.Instructions;
import IR.*;
import Types.*;
import Codegen.*;


public class PrintAssignment extends Instruction{
    private Temp op;
    private Type type;

    public PrintAssignment(Temp op, Type type){
        this.op = op;
        this.type = type;
    }

    public String toString(){
        if(this.type instanceof StringType){
            return "PRINTU "+this.op.toString()+";";
        }
        else{
            return "PRINT"+this.type.toShortString()+" "+this.op.toString()+";";
        }
    }

    public Temp getTemp(){
        return this.op;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}