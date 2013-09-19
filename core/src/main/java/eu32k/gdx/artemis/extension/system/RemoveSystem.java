package eu32k.gdx.artemis.extension.system;

import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.Component;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.base.utils.Bag;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class RemoveSystem extends EntityProcessingSystem {

   private ComponentMapper<PhysicsComponent> pm;
   private ComponentMapper<ActorComponent> am;

   @SuppressWarnings("unchecked")
   public RemoveSystem() {
      super(Aspect.getAspectForAll(RemoveMeComponent.class));
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(PhysicsComponent.class);
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (pm.has(e)) {
         pm.get(e).delete();
      }

      if (am.has(e)) {
         am.get(e).actor.remove();
      }

      Bag<Component> comps = new Bag<Component>();
      e.getComponents(comps);
      for (Component c : comps) {
         Pools.free(c);
      }
      e.deleteFromWorld();
   }

}
