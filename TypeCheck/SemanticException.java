package TypeCheck;

public class SemanticException extends Exception {
    private String message;
    private int line;
    private int offset;

    public SemanticException(String message, int line, int offset) {
        this.message = message;
        this.line = line;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Error:" + this.line + ":" + this.offset + ":" + this.message;
    }
}