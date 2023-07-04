package IR;

public class LabelAllocator {
    private int next;

    public LabelAllocator() {
        this.next = 0;
    }

    public Label allocate() {
        Label l = new Label(this.next);
        this.next++;
        return l;

    }
}