package IR.Instructions;
import IR.*;
import Codegen.*;

public class ConditionalGOTO extends Instruction{
    private Temp cond;
    private Label label;

    public ConditionalGOTO(Temp c, Label l){
        this.cond = c;
        this.label = l;
    }

    public String toString(){
        return "IF "+this.cond.toString()+" GOTO "+"L"+this.label.getNumber()+";";
    }

    public Temp getCond(){
        return this.cond;
    }

    public Label getLabel(){
        return this.label;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}