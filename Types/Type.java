package Types;
import AST.*;
import TypeCheck.*;
public abstract class Type {

    public abstract void accept(Visitor v);

    public abstract Type accept(TypeVisitor v)throws SemanticException;

    public abstract boolean equals(Type t);

    public abstract String toShortString();
    public abstract String toString();
}