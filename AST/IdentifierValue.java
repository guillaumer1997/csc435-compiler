package AST;

import Types.*;
import TypeCheck.*;

public class IdentifierValue {
    private String value;

    public IdentifierValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}