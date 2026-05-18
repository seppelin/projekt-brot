package src.ui;

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
