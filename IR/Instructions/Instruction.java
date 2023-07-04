package IR.Instructions;
import Codegen.*;
import Codegen.*;


public abstract class Instruction{
    public abstract String toString();

    public abstract void accept(CodegenVisitor v);

}