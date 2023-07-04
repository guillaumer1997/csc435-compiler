package IR.Instructions;
import Types.*;
import IR.*;

import Codegen.*;
public class ArrayInitAssignment extends Instruction{
    private Temp assignment;
    private Type type;
    private int size;

    public ArrayInitAssignment(Temp a, Type t, int s){
        this.assignment = a;
        this.type = t;
        this.size = s;

    }

    public Type getType(){
        return this.type;
    }

    public int getSize(){
        return this.size;
    }

    public Temp getTemp(){
        return this.assignment;
    }

    public String toString(){
        return this.assignment.toString()+":="+"NEWARRAY "+this.type.toShortString()+" "+this.size +";";
    }

    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}