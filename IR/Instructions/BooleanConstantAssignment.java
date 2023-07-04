package IR.Instructions;
import Types.*;
import IR.*;
import Codegen.*;

public class BooleanConstantAssignment extends ConstantAssignment{
    private boolean b;
    private Temp t;

    public BooleanConstantAssignment(Temp t, boolean b){
        this.t = t;
        this.b = b;
    }

    public Temp getTemp(){
        return this.t;
    }

    public boolean getVal(){
        return this.b;
        
    }

    public String toString(){
        if(this.b){
            return this.t.toString()+":="+"TRUE;";
        }
        else{
            return this.t.toString()+":="+"FALSE;";
        }
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}