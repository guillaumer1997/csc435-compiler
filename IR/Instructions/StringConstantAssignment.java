package IR.Instructions;
import Types.*;
import IR.*;
import Codegen.*;


public class StringConstantAssignment extends ConstantAssignment{
    private String s;
    private Temp t;

    public StringConstantAssignment(Temp t, String s){
        this.t = t;
        this.s = s;
    }

    public String toString(){
        return this.t.toString()+":="+"\""+s+"\";";
    }

    public Temp getTemp(){
        return this.t;
    }

    public String getVal(){
        return this.s;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}