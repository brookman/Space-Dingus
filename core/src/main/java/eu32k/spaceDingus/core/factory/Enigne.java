package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.sceneGraph.component.LocalTransformComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class Enigne {

   public static Entity createEngine(Entity parent, float x, float y, float rotation, float thrust, int directions, float size) {
      Entity e = EntityFactory.artemisWorld.createEntity();

      e.addComponent(Pools.obtain(TransformComponent.class));
      e.addComponent(Pools.obtain(LocalTransformComponent.class).init(x, y, rotation));
      e.addComponent(Pools.obtain(NodeComponent.class).init(parent));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/engine1.png", size, size, RenderLayer.LOWER_COMPONENTS_2));
      e.addComponent(Pools.obtain(EngineComponent.class).init(thrust, directions));

      e.addToWorld();

      return e;
   }

}
