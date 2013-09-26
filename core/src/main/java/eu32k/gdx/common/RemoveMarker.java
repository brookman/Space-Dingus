package eu32k.gdx.common;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.World;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class RemoveMarker {

   public static void markForRemovalRecursively(Entity entity) {
      World world = entity.getWorld();
      ComponentMapper<ActorComponent> ac = world.getMapper(ActorComponent.class);

      if (ac.has(entity)) {
         EntityActor actor = ac.get(entity).actor;
         for (Actor child : actor.getChildren()) {
            EntityActor childEntity = (EntityActor) child;
            markForRemovalRecursively(childEntity.getEntity());
         }
      }
      entity.addComponent(Pools.get(RemoveMeComponent.class).obtain());
      world.changedEntity(entity);
   }
}
