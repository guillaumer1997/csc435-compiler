package IR.Instructions;
import Types.*;
import IR.*;
import Codegen.*;

public class FloatConstantAssignment extends ConstantAssignment{
    private float f;
    private Temp t;

    public FloatConstantAssignment(Temp t, Float f){
        this.t = t;
        this.f = f;
    }

    public String toString(){
        return this.t.toString()+":="+f+";";
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }

    public Temp getTemp(){
        return this.t;
    }

    public float getVal(){
        return this.f;
    }
}