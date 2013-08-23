package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.component.ActorComponent;

public class General {

   public static Entity createGroupEntity(float x, float y, float rotation, Group parent) {
      return createGenericEntity(new Group(), x, y, rotation, parent);
   }

   public static Entity createActorEntity(float x, float y, float rotation, Group parent) {
      return createGenericEntity(new Actor(), x, y, rotation, parent);
   }

   private static Entity createGenericEntity(Actor actor, float x, float y, float rotation, Group parent) {
      actor.setX(x);
      actor.setY(y);
      actor.setRotation(rotation);
      if (parent == null) {
         EntityFactory.stage.addActor(actor);
      } else {
         parent.addActor(actor);
      }

      Entity e = EntityFactory.artemisWorld.createEntity();
      e.addComponent(Pools.obtain(ActorComponent.class).init(e, actor));
      return e;
   }
}
