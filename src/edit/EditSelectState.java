package src.edit;

import src.game.BuildingType;
import src.ui.SelItemInterface;

// Enum for different edit modes
public enum EditSelectState {
    FieldType,
    Building,
    FillField;

    // Get selectable items for this state
    public SelItemInterface[] getItems() {
        return switch (this) {
            case FieldType, FillField -> src.game.FieldType.values();
            case Building -> BuildingType.values();
        };
    }
}
