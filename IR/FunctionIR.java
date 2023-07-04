package IR;

import Types.*;
import Codegen.*;
import IR.Instructions.*;
import java.util.*;
import java.io.*;

public class FunctionIR {
    private String name;
    private List<Instruction> instructionList;
    private List<Type> params;
    private List<Temp> varList;
    private Type returnType;

    public FunctionIR(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
        this.params = new ArrayList<Type>();
        this.instructionList = new ArrayList<Instruction>();
    }

    public void setParams(List p) {
        this.params = p;
    }

    public List<Type> getParams() {
        return this.params;
    }

    public List<Temp> getVars() {
        return this.varList;
    }

    public List<Instruction> getInstructions() {
        return this.instructionList;
    }

    public void setInstructions(List p) {
        this.instructionList = p;
    }

    public void setVarList(List<Temp> ls) {
        this.varList = ls;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public String getName() {
        return this.name;
    }

    public void accept(CodegenVisitor v) throws IOException {
        v.visit(this);
    }

    public String toString() {
        String paramsToShort = "(";
        for (int i = 0; i < this.params.size(); i++) {
            paramsToShort = paramsToShort + this.params.get(i).toShortString();
        }
        paramsToShort = paramsToShort + ")" + this.returnType.toShortString();

        String result = "FUNC " + this.name + " " + paramsToShort + "\n{\n";

        // handle Temps;

        for (int i = 0; i < this.varList.size(); i++) {
            result = result + "\tTEMP " + this.varList.get(i).getNumber() + ":"
                    + this.varList.get(i).getType().toShortString();
            if (this.varList.get(i).getTempClass() == VarClass.PARAM) {
                result = result + "\t[P(\"" + this.varList.get(i).getName() + "\")]";

            } else if (this.varList.get(i).getTempClass() == VarClass.LOCAL) {
                result = result + "\t[L(\"" + this.varList.get(i).getName() + "\")]";

            }
            result = result + ";\n";
        }

        for (int i = 0; i < this.instructionList.size(); i++) {
            if (!(this.instructionList.get(i) instanceof Label)) {
                result = result + "\t";
            }
            result = result + this.instructionList.get(i).toString();
            result = result + "\n";
        }

        result = result + "}";

        return result;

    }

}