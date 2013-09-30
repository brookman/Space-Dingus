package eu32k.gdx.artemis.extension.factory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;

public class Factory {

   protected ExtendedWorld world;
   protected Stage stage;

   public Factory(ExtendedWorld world, Stage stage) {
      this.world = world;
      this.stage = stage;
   }

   protected Entity createActorEntity(Vector2 position, float width, float height, float rotation) {
      return createActorEntity(position.x, position.y, width, height, rotation, null);
   }

   protected Entity createActorEntity(float x, float y, float width, float height, float rotation) {
      return createActorEntity(x, y, width, height, rotation, null);
   }

   protected Entity createActorEntity(Vector2 position, float width, float height, float rotation, Group parent) {
      return createActorEntity(position.x, position.y, width, height, rotation, parent);
   }

   protected Entity createActorEntity(float x, float y, float width, float height, float rotation, Group parent) {
      Entity e = world.createEntity();

      EntityActor actor = new EntityActor(world, e);
      actor.setOriginX(0);
      actor.setOriginY(0);
      actor.setX(x);
      actor.setY(y);
      actor.setWidth(width);
      actor.setHeight(height);
      actor.setRotation(rotation);

      if (parent == null) {
         stage.addActor(actor);
      } else {
         parent.addActor(actor);
      }

      e.addComponent(get(ActorComponent.class).init(actor));
      return e;
   }

   protected static <T> T get(Class<T> type) {
      return Pools.obtain(type);
   }
}
