package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;
import eu32k.spaceDingus.core.factory.Bullet;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class WeaponSystem extends EntityProcessingSystem {

   private ComponentMapper<WeaponComponent> wm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<SpeedComponent> sm;
   private ComponentMapper<NodeComponent> nm;
   private ComponentMapper<PhysicsComponent> phm;

   @SuppressWarnings("unchecked")
   public WeaponSystem() {
      super(Aspect.getAspectForAll(WeaponComponent.class, TransformComponent.class));
   }

   @Override
   protected void initialize() {
      wm = world.getMapper(WeaponComponent.class);
      tm = world.getMapper(TransformComponent.class);
      sm = world.getMapper(SpeedComponent.class);
      nm = world.getMapper(NodeComponent.class);
      phm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      WeaponComponent weaponComponent = wm.get(e);

      Vector2 position = new Vector2(tm.get(e).x, tm.get(e).y);
      Vector2 velocity = new Vector2(weaponComponent.targetX - position.x, weaponComponent.targetY - position.y);
      tm.get(e).setRotation(MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees);

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

      if (nm.has(e)) {
         Entity parent = nm.get(e).getParent();

         if (phm.has(parent)) {
            velocity.add(phm.get(parent).body.getLinearVelocity());
         }
      }

      Bullet.createRocket(position, velocity.cpy().scl(0.2f), rot);

      // if (MathUtils.randomBoolean()) {
      // EntityFactory.createBullet(position, velocity, rot);
      // } else {
      // Bullet.createBullet2(position, velocity, rot);
      // }

      weaponComponent.shoot();
   }
}
