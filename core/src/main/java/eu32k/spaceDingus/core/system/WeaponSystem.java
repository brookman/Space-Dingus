package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;
import eu32k.spaceDingus.core.factory.BulletFactory;
import eu32k.spaceDingus.core.factory.MiscFactory;

public class WeaponSystem extends EntityProcessingSystem {

   private BulletFactory bf;
   private MiscFactory mf;

   private ComponentMapper<WeaponComponent> wm;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<SpeedComponent> sm;
   private ComponentMapper<PhysicsComponent> phm;

   private Vector2 velocity;

   @SuppressWarnings("unchecked")
   public WeaponSystem(BulletFactory bf, MiscFactory mf) {
      super(Aspect.getAspectForAll(WeaponComponent.class, ActorComponent.class));
      this.bf = bf;
      this.mf = mf;
      velocity = new Vector2();
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

      velocity.set(weaponComponent.targetX - stagePosition.x, weaponComponent.targetY - stagePosition.y);
      float targetDirection = MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees;

      if (weaponComponent.canRotate) {
         actor.setRotation(targetDirection - parent.getRotationOnStage());
      }

      // DebugRenderer.begin(ShapeType.Line);
      // DebugRenderer.getRenderer().setColor(1, 1, 1, 0.5f);
      // DebugRenderer.getRenderer().line(stagePosition, new Vector2(stagePosition).add(new Vector2(velocity).nor().scl(10.0f)));
      // DebugRenderer.end();

      if (!weaponComponent.shouldShoot()) {
         return;
      }

      if (phm.has(parent.getEntity())) {
         Body parentBody = phm.get(parent.getEntity()).body;
         if (weaponComponent.bulletType == WeaponComponent.BULLET_TYPE_ROCKET) {
            velocity.set(parentBody.getLinearVelocity());
            velocity.add(MathUtils.cos(actor.getRotationOnStage() * MathUtils.degreesToRadians), MathUtils.sin(actor.getRotationOnStage() * MathUtils.degreesToRadians));
            bf.createRocket(stagePosition, velocity, actor.getRotationOnStage() * MathUtils.degreesToRadians);
            weaponComponent.shoot();
         } else if (weaponComponent.bulletType == WeaponComponent.BULLET_TYPE_NORMAL) {

            velocity.rotate((MathUtils.random() * weaponComponent.precision - weaponComponent.precision / 2.0f) * 360);
            float rot = MathUtils.atan2(velocity.y, velocity.x);

            if (sm.has(e)) {
               velocity.nor().scl(sm.get(e).speed);
            } else {
               velocity.scl(0.0f);
            }

            velocity.add(parentBody.getLinearVelocity());
            bf.createBullet(stagePosition, velocity, rot);
            mf.createMuzzleFlash(0, 0, rot * MathUtils.radiansToDegrees, actor);
            weaponComponent.shoot();
         }
      }
   }
}
