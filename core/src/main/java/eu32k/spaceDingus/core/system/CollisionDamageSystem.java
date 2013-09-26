package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.VoidEntitySystem;
import eu32k.gdx.common.RemoveMarker;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.HealthComponent;

public class CollisionDamageSystem extends VoidEntitySystem implements ContactListener {

   private ComponentMapper<DamageComponent> dm;
   private ComponentMapper<HealthComponent> hm;

   private World box2dWorld;

   public CollisionDamageSystem(World box2dWorld) {
      this.box2dWorld = box2dWorld;
   }

   @Override
   protected void initialize() {
      dm = world.getMapper(DamageComponent.class);
      hm = world.getMapper(HealthComponent.class);
      box2dWorld.setContactListener(this);
   }

   @Override
   protected void processSystem() {
      // NOP
   }

   private void handleCollision(Entity damager, Entity damaged) {
      if (dm.has(damager) && hm.has(damaged)) {
         hm.get(damaged).health -= dm.get(damager).damage;
         hm.get(damaged).health = Math.max(hm.get(damaged).health, 0);
         if (dm.get(damager).nonrecurring) {
            // bodyA.setLinearDamping(1.0f);
            // bodyA.setAngularDamping(1.0f);
            dm.get(damager).damage = 0;
         }
         if (dm.get(damager).removeAfterDamage) {
            RemoveMarker.markForRemovalRecursively(damager);
         }
      }
   }

   @Override
   public void beginContact(Contact contact) {
      if (!contact.isTouching()) {
         return;
      }

      Body bodyA = contact.getFixtureA().getBody();
      Body bodyB = contact.getFixtureB().getBody();
      Entity entityA = (Entity) contact.getFixtureA().getUserData();
      Entity entityB = (Entity) contact.getFixtureB().getUserData();

      handleCollision(entityA, entityB);
      handleCollision(entityB, entityA);
   }

   @Override
   public void endContact(Contact contact) {
      // NOP
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold) {
      // NOP
   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse) {
      // NOP
   }
}
