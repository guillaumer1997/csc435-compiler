package IR.Instructions;
import IR.*;
import java.util.*;
import Codegen.*;

public class CallInstruction extends Instruction{
    FunctionIR func;
    List<Temp> operands;

    public CallInstruction(FunctionIR func, List<Temp> operands){
        this.func = func;
        this.operands = operands;
    }

    public String toString(){
        
        String result = "";
        result = result+"CALL "+this.func.getName()+"(";
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


    public void accept(CodegenVisitor v){
        v.visit(this);
    }


}