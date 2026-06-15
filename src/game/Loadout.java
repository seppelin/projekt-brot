package src.game;

import java.io.*;

public class Loadout implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private static Loadout loadout;

    // Start values
    int gems = 20;
    SkinType currentSkin = SkinType.Default;
    SkinType[] skins = {SkinType.Default};
    WeaponType currentWeapon = WeaponType.Knife;
    WeaponType[] weapons = {WeaponType.Knife};
    AbilityType currentAbility = AbilityType.None;
    AbilityType[] abilities = {AbilityType.None};

    public static AbilityType[] getAbilities() {
        return loadout.abilities;
    }

    public static int getGems() {
        return loadout.gems;
    }

    public static SkinType[] getSkins() {
        return loadout.skins;
    }

    public static WeaponType[] getWeapons() {
        return loadout.weapons;
    }

    public static void init() {
        try (var in = new ObjectInputStream(new FileInputStream("resources/loadout.savedata"))) {
            loadout = (Loadout) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading loadout, generating new and empty one");
            loadout = new Loadout();
            Loadout.save();
        }
    }

    private static void save() {
        try (var out = new ObjectOutputStream(new FileOutputStream("resources/loadout.savedata"))) {
            out.writeObject(loadout);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WeaponType getCurrentWeapon() {
        return currentWeapon;
    }

    public SkinType getCurrentSkin() {
        return currentSkin;
    }

    public AbilityType getCurrentAbility() {
        return currentAbility;
    }
}
