package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class Weapon {

   public static Entity createWeapon(Group parent, float x, float y) {
      Entity e = General.createActorEntity(x, y, 0, parent);

      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/gun.png", 0.2f, 0.2f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(WeaponComponent.class).init(MathUtils.random(100, 200)));
      e.addComponent(Pools.obtain(SpeedComponent.class).init(7.0f));

      e.addToWorld();

      return e;
   }
}
