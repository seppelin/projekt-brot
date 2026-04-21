package de.developergang;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class MapFactory implements EntityFactory {

    @Spawns("grass")
    public Entity newGrass(SpawnData data) {
        return entityBuilder(data)
                .view("grass_tile.png")
                .zIndex(0) // Bottom layer
                .buildAndAttach();
    }

    @Spawns("cliff")
    public Entity newCliff(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.WALL) // For collision logic
                .view("cliff_face.png")
                .bbox(new HitBox(BoundingShape.box(32, 32)))
                .with(new CollidableComponent(true))
                .zIndex(10) // Middle layer
                .buildAndAttach();
    }
}
