package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.common.Time;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class ExpireSystem extends EntityProcessingSystem {

   private ComponentMapper<ExpireComponent> em;
   private ComponentMapper<ActorComponent> ac;

   @SuppressWarnings("unchecked")
   public ExpireSystem() {
      super(Aspect.getAspectForAll(ExpireComponent.class));
   }

   @Override
   protected void initialize() {
      em = world.getMapper(ExpireComponent.class);
      ac = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (Time.getTime() >= em.get(e).expireAt) {
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
