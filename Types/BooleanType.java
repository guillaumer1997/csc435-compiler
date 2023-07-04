package Types;
import AST.*;
import TypeCheck.*;
public class BooleanType extends Type{
    public String toString(){
        return "boolean";
    }

    public void accept(Visitor v){
        v.visit(this);
    }

    public Type accept(TypeVisitor v)throws SemanticException{
        return v.visit(this);
    }


    public boolean equals(Type t){
        if (t instanceof BooleanType){
            return true;
        }
        return false;
    }

    public String toShortString(){
        return "Z";
    }

}