package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.component.DeathAnimationComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class RemoveSystem extends EntityProcessingSystem {

   private ComponentMapper<PhysicsComponent> pm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<DeathAnimationComponent> dm;

   @SuppressWarnings("unchecked")
   public RemoveSystem() {
      super(Aspect.getAspectForAll(RemoveMeComponent.class));
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(PhysicsComponent.class);
      tm = world.getMapper(TransformComponent.class);
      dm = world.getMapper(DeathAnimationComponent.class);
   }

   @Override
   protected void process(Entity e) {

      if (dm.has(e) && tm.has(e)) {
         TransformComponent pos = tm.get(e);
         dm.get(e).createAnimation(pos.x, pos.y, pos.getRotation());
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
