package IR.Instructions;
import Types.*;
import IR.*;
import Codegen.*;


public class IntConstantAssignment extends ConstantAssignment{
    private int i;
    private Temp t;

    public IntConstantAssignment(Temp t, int i){
        this.t = t;
        this.i = i;
    }

    public String toString(){
        return this.t.toString()+":="+i+";";
    }

    public Temp getTemp(){
        return this.t;
    }

    public int getVal(){
        return this.i;
    }



    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}