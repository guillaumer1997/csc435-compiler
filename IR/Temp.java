package IR;

import Types.*;

public class Temp {
    private int number;
    private Type type;
    private VarClass varClass;
    private String id;

    public Temp(String id, int number, Type type, VarClass varClass) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.varClass = varClass;
    }

    public Temp(int number, Type type, VarClass varClass) {
        this.number = number;
        this.type = type;
        this.varClass = varClass;
    }

    public String getName() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public VarClass getTempClass() {
        return this.varClass;
    }

    public int getNumber() {
        return this.number;
    }

    public String toString() {
        return "T" + number;
    }
}