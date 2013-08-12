package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;
import eu32k.spaceDingus.core.sceneGraph.component.LocalTransformComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class Weapon {

   public static Entity createWeapon(Entity parent, float x, float y) {
      Entity e = EntityFactory.artemisWorld.createEntity();

      e.addComponent(Pools.obtain(TransformComponent.class).init());
      e.addComponent(Pools.obtain(LocalTransformComponent.class).init(x, y, 0));
      e.addComponent(Pools.obtain(NodeComponent.class).init(parent));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/gun.png", 0.2f, 0.2f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(WeaponComponent.class).init(MathUtils.random(100, 200)));
      e.addComponent(Pools.obtain(SpeedComponent.class).init(7.0f));

      e.addToWorld();

      return e;
   }
}
