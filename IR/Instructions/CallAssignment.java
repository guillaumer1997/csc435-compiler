package IR.Instructions;
import IR.*;
import java.util.*;
import Codegen.*;

public class CallAssignment extends Instruction{
    FunctionIR func;
    List<Temp> operands;
    Temp assignment;

    public CallAssignment(Temp assignment, FunctionIR func, List<Temp> operands){
        this.assignment = assignment;
        this.func = func;
        this.operands = operands;
    }

    public String toString(){
        String result = "";
        result = result+assignment.toString()+":="+"CALL "+this.func.getName()+"(";
        for (int i=0; i<this.operands.size();i++){
            result=result+this.operands.get(i).toString()+" ";
        }
        result = result+");";

        return result;

        
    }

    public List<Temp> getTemps(){
        return this.operands;
    }

    public FunctionIR getFunc(){
        return this.func;
    }

    public Temp getLeft(){
        return this.assignment;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }


}