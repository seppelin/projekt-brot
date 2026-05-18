package src.edit;

import src.game.ItemType;
import src.ui.SelItemInterface;

public enum EditSelectState {
    FieldType,
    Building,
    FillField;

    public SelItemInterface[] getItems() {
        return switch (this) {
            case FieldType, FillField -> src.game.FieldType.values();
            case Building -> ItemType.values();
        };
    }
}
