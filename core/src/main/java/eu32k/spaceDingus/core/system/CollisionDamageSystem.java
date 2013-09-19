package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.VoidEntitySystem;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.DamageComponent;

public class CollisionDamageSystem extends VoidEntitySystem {

   private ComponentMapper<DamageComponent> dm;
   private ComponentMapper<DamagableComponent> dem;

   private World box2dWorld;

   public CollisionDamageSystem(World box2dWorld) {
      this.box2dWorld = box2dWorld;
   }

   @Override
   protected void initialize() {
      dm = world.getMapper(DamageComponent.class);
      dem = world.getMapper(DamagableComponent.class);
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }

   @Override
   protected void processSystem() {
      for (Contact contact : box2dWorld.getContactList()) {
         Body bodyA = contact.getFixtureA().getBody();
         Body bodyB = contact.getFixtureB().getBody();
         Entity entityA = (Entity) bodyA.getUserData();
         Entity entityB = (Entity) bodyB.getUserData();

         Vector2 resulting = new Vector2(bodyA.getLinearVelocity()).sub(bodyB.getLinearVelocity());
         float speed = resulting.len();

         if (dm.has(entityA) && dem.has(entityB)) {
            dem.get(entityB).currentDamage += dm.get(entityA).damage * speed;
            if (dm.get(entityA).nonrecurring) {
               bodyA.setLinearDamping(1.0f);
               bodyA.setAngularDamping(1.0f);
               dm.get(entityA).damage = 0;
            }
         }
         if (dm.has(entityB) && dem.has(entityA)) {
            dem.get(entityA).currentDamage += dm.get(entityB).damage * speed;
            if (dm.get(entityB).nonrecurring) {
               bodyB.setLinearDamping(1.0f);
               bodyB.setAngularDamping(1.0f);
               dm.get(entityB).damage = 0;
            }
         }
      }
   }
}
