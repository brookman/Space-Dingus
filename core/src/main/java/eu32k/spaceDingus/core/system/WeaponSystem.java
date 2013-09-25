package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.Factory;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class WeaponSystem extends EntityProcessingSystem {

   private Factory factory;

   private ComponentMapper<WeaponComponent> wm;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<SpeedComponent> sm;
   private ComponentMapper<PhysicsComponent> phm;

   @SuppressWarnings("unchecked")
   public WeaponSystem(Factory factory) {
      super(Aspect.getAspectForAll(WeaponComponent.class, ActorComponent.class));
      this.factory = factory;
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

      EntityActor actor = am.get(e).actor;
      EntityActor parent = (EntityActor) actor.getParent();

      Vector2 stagePosition = actor.getPositionOnStage();

      Vector2 velocity = new Vector2(weaponComponent.targetX - stagePosition.x, weaponComponent.targetY - stagePosition.y);
      float targetDirection = MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees;

      actor.setRotation(targetDirection - parent.getRotationOnStage());

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

      if (phm.has(parent.getEntity())) {
         Vector2 rocketVelocity = new Vector2(phm.get(parent.getEntity()).body.getLinearVelocity());
         rocketVelocity.add(new Vector2(velocity).nor().scl(0.5f));
         factory.createRocket(stagePosition, rocketVelocity, rot);
         velocity.add(phm.get(parent.getEntity()).body.getLinearVelocity());
      }

      // if (velocity.len() > 7) {
      // System.out.println(velocity.len());
      // }
      factory.createBullet(stagePosition, velocity, rot);
      factory.createMuzzleFlash(0, 0, rot * MathUtils.radiansToDegrees, actor);

      // if (MathUtils.randomBoolean()) {
      // EntityFactory.createBullet(position, velocity, rot);
      // } else {
      // Bullet.createBullet2(position, velocity, rot);
      // }

      weaponComponent.shoot();
   }
}
