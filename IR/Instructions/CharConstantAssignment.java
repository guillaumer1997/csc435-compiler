package IR.Instructions;
import Types.*;
import IR.*;
import Codegen.*;

public class CharConstantAssignment extends ConstantAssignment{
    private char c;
    private Temp t;

    public CharConstantAssignment(Temp t, char c){
        this.t = t;
        this.c = c;
    }

    public String toString(){
        return this.t.toString()+":="+"'"+c+"';";
    }


    public char getVal(){
        return this.c;
    }

    public Temp getTemp(){
        return this.t;
    }



    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}