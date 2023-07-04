package IR.Instructions;
import IR.*;
import Codegen.*;


public class UnconditionalGOTO extends Instruction{
    private Label label;

    public UnconditionalGOTO(Label l){
        this.label = l;
    }

    public String toString(){
        return "GOTO "+"L"+this.label.getNumber()+";";
    }

    public Label getLabel(){
        return this.label;
    }


    public void accept(CodegenVisitor v){
        v.visit(this);
    }
}