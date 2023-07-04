package IR;

import java.util.*;

public class ProgramIR {
    private String name;

    private List<FunctionIR> funcs;

    public ProgramIR() {
        this.funcs = new ArrayList<FunctionIR>();

    }

    public void addFunction(FunctionIR func) {
        this.funcs.add(func);
    }

    public List<FunctionIR> getFunctions() {
        return this.funcs;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        String result = "PROG " + this.name + "\n";
        for (int i = 0; i < funcs.size(); i++) {
            result = result + this.funcs.get(i).toString();
            result = result + "\n";
        }

        return result;

    }

}