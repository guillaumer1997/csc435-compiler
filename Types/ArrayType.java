package Types;
import AST.*;
import TypeCheck.*;
public class ArrayType extends Type{
    private Type type;
    private int size;

    public ArrayType(Type type, int size){
        this.type = type;
        this.size = size;
    }

    public Type getType(){
        return this.type;
    }

    public int getSize(){
        return this.size;
    }


    public void accept(Visitor v){
        v.visit(this);
    }

    public Type accept(TypeVisitor v)throws SemanticException{
        return v.visit(this);
    }

    public boolean equals(Type t){
        if (t instanceof ArrayType){
            if((((ArrayType)t).getSize() == this.getSize()) && (((ArrayType)t).getType().equals(this.getType()))){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return this.getType().toString()+"["+this.getSize()+"]";
    }

    public String toShortString(){
        return "A"+this.type.toShortString();
    }

}