package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;
import eu32k.spaceDingus.core.factory.Bullet;

public class WeaponSystem extends EntityProcessingSystem {

   private ComponentMapper<WeaponComponent> wm;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<SpeedComponent> sm;
   private ComponentMapper<PhysicsComponent> phm;

   @SuppressWarnings("unchecked")
   public WeaponSystem() {
      super(Aspect.getAspectForAll(WeaponComponent.class, ActorComponent.class));
   }

   @Override
   protected void initialize() {
      wm = world.getMapper(WeaponComponent.class);
      am = world.getMapper(ActorComponent.class);
      sm = world.getMapper(SpeedComponent.class);
      phm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      WeaponComponent weaponComponent = wm.get(e);

      Actor actor = am.get(e).actor;
      Vector2 position = new Vector2(actor.getX(), actor.getY());
      Vector2 velocity = new Vector2(weaponComponent.targetX - position.x, weaponComponent.targetY - position.y);
      actor.setRotation(MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees);

      if (!weaponComponent.shouldShoot()) {
         return;
      }

      velocity.rotate((MathUtils.random() * weaponComponent.precision - weaponComponent.precision / 2.0f) * 360);
      float rot = MathUtils.atan2(velocity.y, velocity.x);

      if (sm.has(e)) {
         velocity.nor().scl(sm.get(e).speed);
      } else {
         velocity.scl(0.0f);
      }

      if (velocity.len() > 7) {
         System.out.println(velocity.len());
      }
      Bullet.createBullet(position, velocity, rot);

      // if (am.has(e)) {
      // Group parent = am.get(e).actor.getParent();
      // Entity parentEntity = parent.
      //
      // if (phm.has(parent)) {
      // velocity.add(phm.get(parent).body.getLinearVelocity());
      // Bullet.createBullet(position, velocity, rot);
      // // Bullet.createRocket(position, phm.get(parent).body.getLinearVelocity(), rot);
      // Misc.createMuzzleFlash(position.x, position.y, rot * MathUtils.radiansToDegrees);
      // }
      // }

      // Bullet.createRocket(position, velocity.cpy().scl(0.0f), rot);

      // if (MathUtils.randomBoolean()) {
      // EntityFactory.createBullet(position, velocity, rot);
      // } else {
      // Bullet.createBullet2(position, velocity, rot);
      // }

      weaponComponent.shoot();
   }
}
