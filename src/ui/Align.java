package src.ui;

// Alignment options for layouts
public enum Align {
    Start,
    Middle,
    End;

    public static Align fromIndex(int i) {
        return values()[i];
    }

    public int index() {
        return ordinal();
    }
}
