package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class DeathSystem extends EntityProcessingSystem {

   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<ActorComponent> ac;

   @SuppressWarnings("unchecked")
   public DeathSystem() {
      super(Aspect.getAspectForAll(HealthComponent.class));
   }

   @Override
   protected void initialize() {
      hm = world.getMapper(HealthComponent.class);
      ac = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (hm.get(e).health <= 0) {
         removeRecursively(e);
      }
   }

   private void removeRecursively(Entity entity) {
      if (ac.has(entity)) {
         EntityActor actor = ac.get(entity).actor;
         for (Actor child : actor.getChildren()) {
            EntityActor childEntity = (EntityActor) child;
            removeRecursively(childEntity.getEntity());
         }
      }
      entity.addComponent(Pools.get(RemoveMeComponent.class).obtain());
      world.changedEntity(entity);
   }
}
