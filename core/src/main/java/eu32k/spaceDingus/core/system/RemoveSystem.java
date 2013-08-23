package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.DeathAnimationComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class RemoveSystem extends EntityProcessingSystem {

   private ComponentMapper<PhysicsComponent> pm;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<DeathAnimationComponent> dm;

   @SuppressWarnings("unchecked")
   public RemoveSystem() {
      super(Aspect.getAspectForAll(RemoveMeComponent.class));
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(PhysicsComponent.class);
      am = world.getMapper(ActorComponent.class);
      dm = world.getMapper(DeathAnimationComponent.class);
   }

   @Override
   protected void process(Entity e) {

      if (dm.has(e) && am.has(e)) {
         Actor actor = am.get(e).actor;
         dm.get(e).createAnimation(actor.getX(), actor.getY(), actor.getRotation());
      }

      if (pm.has(e)) {
         pm.get(e).delete();
      }

      Bag<Component> comps = new Bag<Component>();
      e.getComponents(comps);
      for (Component c : comps) {
         Pools.free(c);
      }
      e.deleteFromWorld();
   }

}
