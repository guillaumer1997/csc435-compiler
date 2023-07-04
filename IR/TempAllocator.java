package IR;

import Types.*;
import java.util.*;

public class TempAllocator {
    private final int MAX_LOCALS = 2;
    private List<Temp> temps;
    private int next;

    public TempAllocator() {
        this.next = 0;
        this.temps = new ArrayList<Temp>();
    }

    public Temp allocateTemp(Type t, VarClass varClass) {
        Temp temp = new Temp(this.next, t, varClass);
        this.temps.add(temp);
        this.next++;
        return temp;

    }

    public Temp allocateVar(String id, Type t, VarClass varClass) {
        Temp temp = new Temp(id, this.next, t, varClass);
        this.temps.add(temp);
        this.next++;
        return temp;
    }

    public List<Temp> getTemps() {
        return this.temps;
    }

}