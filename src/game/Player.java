package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.Vector2I;
import src.scenes.PlayScene;
import src.ui.InputHandle;

import static com.raylib.Raylib.*;

public class Player {
    final float weaponCooldown = 0.3f;
    final int weaponDamage = 70;
    float currentCooldown = 0;

    WeaponType weapon;
    SkinType skin;
    AbilityType ability;
    Animation anim;
    private Vector2I size = new Vector2I(10, 15);
    private Vector2I weaponSize = new Vector2I(12, 3);
    private Vector2 position;
    private Vector2 velocity;

    public Player(int startX, int startY) {
        this.weapon = Loadout.getCurrentWeapon();
        this.skin = Loadout.getCurrentSkin();
        this.ability = Loadout.getCurrentAbility();
        velocity = new Vector2();
        // Convert grid position to pixel coordinates (16px per field)
        position = Helpers.newVector2(startX * 16, startY * 16);
        anim = new Animation(skin.getAnimTexture(), 16, 0.2f);
    }

    public Vector2 getPosition() {
        return position;
    }

    // Handle player movement input
    private void handleInput(InputHandle ih) {
        velocity = Helpers.newVector2(0, 0);
        if (IsKeyDown(KEY_W)) {
            velocity.y(-1);
        }
        if (IsKeyDown(KEY_S)) {
            velocity.y(1);
        }
        if (IsKeyDown(KEY_A)) {
            velocity.x(-1);
        }
        if (IsKeyDown(KEY_D)) {
            velocity.x(1);
        }
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 1);
        if (Vector2LengthSqr(velocity) > 0) {
            anim.progress();
        } else {
            anim.stop();
        }
    }

    // Update position without map collision (for menu)
    public void updateNoMap(InputHandle ih) {
        var start1 = Helpers.newVector2(174, 114);
        var end1 = Helpers.newVector2(274, 193);
        var start2 = Helpers.newVector2(148, 193);
        var end2 = Helpers.newVector2(243, 276);

        handleInput(ih);
        var oldPos = position;
        position = Vector2Add(position, velocity);

        // Prevent clipping between zones
        var oldBigger = oldPos.y() - 193 > 0;
        var newBigger = position.y() - 193 > 0;
        if (oldBigger != newBigger && (oldPos.x() < start1.x() || oldPos.x() > end2.x())) {
            position.y(oldPos.y());
        }

        // Clamp position to boundaries
        if (position.y() <= 193) {
            position = Vector2Clamp(position, start1, end1);
        } else {
            position = Vector2Clamp(position, start2, end2);
        }
    }

    // Update position with map collision checking
    public void update(InputHandle inputHandle, Map map, Camera camera, PlayScene play) {
        handleInput(inputHandle);
        position = Vector2Add(position, velocity);
        position = map.nearestValidPosition(position);
        updateWeapon(camera, play);
    }

    public void draw(Camera camera) {
        var centeredRect = size.centeredRect(position);
        anim.draw(centeredRect);
        drawWeapon(camera);
    }

    private void updateWeapon(Camera camera, PlayScene play) {
        if (currentCooldown == 0) {
            if (Raylib.IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
                currentCooldown = weaponCooldown;
            }
        } else {
            var oldCooldown = currentCooldown;
            currentCooldown -= Raylib.GetFrameTime();
            currentCooldown = Math.max(0, currentCooldown);
            if (oldCooldown > weaponCooldown / 2 && currentCooldown < weaponCooldown / 2) {
                Vector2 mousePos = GetScreenToWorld2D(GetMousePosition(), camera);

                Vector2 dir = Vector2Subtract(mousePos, position);
                Vector2 dirNormalized = Vector2Normalize(dir);
                var rad = (float) Math.sin(currentCooldown / weaponCooldown * Math.PI) * 6;
                var swordTail = Vector2Add(position, Vector2Scale(dirNormalized, 2.0f + rad));
                var swordEnd = Vector2Add(position, Vector2Scale(dirNormalized, 2.0f + rad + weaponSize.x));

                for (var e : play.enemies) {
                    if (CheckCollisionCircleLine(e.getPosition(), 8, swordTail, swordEnd)) {
                        if (e.dealDamage(weaponDamage)) {
                            play.addKillMoney(8);
                        }
                    }
                }
            }
        }
    }

    private void drawWeapon(Camera camera) {
        var wt = weapon.getTexture();
        Vector2 mousePos = GetScreenToWorld2D(GetMousePosition(), camera);

        Vector2 dir = Vector2Subtract(mousePos, position);
        double angle = Math.atan2(dir.y(), dir.x()) * RAD2DEG;

        var rad = (float) Math.sin(currentCooldown / weaponCooldown * Math.PI) * 6;
        // position from the center
        Vector2 dirNormalized = Vector2Normalize(dir);
        Vector2 arrowPosition = Vector2Add(position, Vector2Scale(dirNormalized, 2.0f + rad));

        Rectangle sourceRec = Helpers.newRectangle(0.0f, 0.0f, (float) wt.width(), (float) wt.height());

        // dest defines where it goes on screen and its size
        Rectangle destRec = Helpers.newRectangle(arrowPosition.x(), arrowPosition.y(), weaponSize.x, weaponSize.y);

        // rotation origin
        Vector2 origin = Helpers.newVector2(0.0f, (float) weaponSize.y / 2.0f);

        DrawTexturePro(weapon.getTexture(), sourceRec, destRec, origin, (float) angle, Colors.WHITE);
    }
}
