package eu32k.spaceDingus.core.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;
import eu32k.gdx.artemis.extension.factory.Factory;
import eu32k.gdx.common.Bits;
import eu32k.gdx.common.Textures;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class AccessoryFactory extends Factory {

   public AccessoryFactory(ExtendedWorld world, Stage stage) {
      super(world, stage);
   }

   public Entity createEngine(Group parent, float x, float y, float rotation, float thrust, int directions, float size) {
      Entity e = createActorEntity(x, y, 1.0f, 1.0f, rotation, parent);

      e.addComponent(Pools.obtain(EngineComponent.class).init(thrust, directions));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/engine1.png"))));

      e.addToWorld();

      Group g = world.getMapper(ActorComponent.class).get(e).actor;
      g.setScale(size);

      Entity pe = createActorEntity(-0.57f, 0, 0.9f, 0.9f, 0, g);
      pe.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/fire.png"))));

      pe.addToWorld();

      return e;
   }

   public Entity createWeapon(Group parent, float x, float y, float rotation, int shootDelay, int bulletType, boolean canRotate) {
      Entity e = createActorEntity(x, y, 0.2f, 0.2f, rotation, parent);

      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/gun.png"))));
      e.addComponent(Pools.obtain(WeaponComponent.class).init(shootDelay, bulletType, canRotate));
      e.addComponent(Pools.obtain(SpeedComponent.class).init(7.0f));

      e.addToWorld();

      return e;
   }

   public Entity createShield(Group parent, Bits bits, Fixture fixture, float shield) {
      Entity e = createActorEntity(0f, 0f, 1.15f, 1.15f, 0, parent);

      fixture.setUserData(e);

      e.addComponent(Pools.obtain(ShieldComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(shield));
      e.addComponent(Pools.obtain(PhysicsShieldComponent.class).init(bits, fixture));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/shield.png"))));

      e.addToWorld();
      return e;
   }
}
