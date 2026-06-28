package src.game;

import java.io.*;
import java.util.Arrays;

public class Loadout implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private static Loadout loadout;

    // Default player loadout values
    private int gems = 20;
    private SkinType currentSkin = SkinType.Default;
    private SkinType[] skins = {SkinType.Default};
    private WeaponType currentWeapon = WeaponType.Knife;
    private WeaponType[] weapons = {WeaponType.Knife};
    private AbilityType currentAbility = AbilityType.None;
    private AbilityType[] abilities = {AbilityType.None};

    public static AbilityType[] getAbilities() {
        return loadout.abilities;
    }

    public static int getGems() {
        return loadout.gems;
    }

    public static void addGems(int gems) {
        loadout.gems += gems;
        save();
    }

    public static void addSkin(SkinType skin) {
        loadout.skins = Arrays.copyOf(loadout.skins, loadout.skins.length + 1);
        loadout.skins[loadout.skins.length - 1] = skin;
        save();
    }

    public static SkinType[] getSkins() {
        return loadout.skins;
    }

    public static WeaponType[] getWeapons() {
        return loadout.weapons;
    }

    // Load loadout from file or create new one
    public static void init() {
        try (var in = new ObjectInputStream(new FileInputStream("resources/loadout.savedata"))) {
            loadout = (Loadout) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading loadout, generating new and empty one");
            loadout = new Loadout();
            Loadout.save();
        }
    }

    // Save loadout to file
    public static void save() {
        try (var out = new ObjectOutputStream(new FileOutputStream("resources/loadout.savedata"))) {
            out.writeObject(loadout);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static WeaponType getCurrentWeapon() {
        return loadout.currentWeapon;
    }

    public static SkinType getCurrentSkin() {
        return loadout.currentSkin;
    }

    public static AbilityType getCurrentAbility() {
        return loadout.currentAbility;
    }

    public static void setSkin(SkinType skin) {
        if (Arrays.asList(loadout.skins).contains(skin)) {
            loadout.currentSkin = skin;
        }
    }
}
