package src.ui;

public class AlignItem {
    LayoutInterface iface;
    Align align;
    int assignedSpace = 0;

    public AlignItem(LayoutInterface iface, Align align) {
        this.iface = iface;
        this.align = align;
    }
}
