package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.MyGroup;
import eu32k.spaceDingus.core.component.ActorComponent;

public class General {

   // public static Entity createGroupEntity(float x, float y, float rotation, Group parent) {
   // return createGenericEntity(new Group(), x, y, rotation, parent);
   // }

   public static Entity createActorEntity(float x, float y, float width, float height, float rotation, Group parent, TextureRegion region) {
      Actor actor = new MyGroup(region);
      actor.setX(x);
      actor.setY(y);
      actor.setWidth(width);
      actor.setHeight(height);
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
