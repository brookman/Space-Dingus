package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class Enigne {

   public static Entity createEngine(Group parent, float x, float y, float rotation, float thrust, int directions, float size) {
      Entity e = General.createActorEntity(x, y, rotation, parent);

      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/engine1.png", size, size, RenderLayer.LOWER_COMPONENTS_2));
      e.addComponent(Pools.obtain(EngineComponent.class).init(thrust, directions));

      e.addToWorld();

      return e;
   }

}
