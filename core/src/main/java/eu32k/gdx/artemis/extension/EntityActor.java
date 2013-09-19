package eu32k.gdx.artemis.extension;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.World;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;

public class EntityActor extends Group {
   private Entity entity;
   private ComponentMapper<TextureRegionComponent> trc;

   public EntityActor(World world, Entity entity) {
      this.entity = entity;
      trc = world.getMapper(TextureRegionComponent.class);
   }

   public Entity getEntity() {
      return entity;
   }

   @Override
   public void draw(SpriteBatch batch, float parentAlpha) {

      Color color = getColor();
      float alpha = color.a * parentAlpha;
      if (trc.has(entity)) {
         batch.setColor(color.r, color.g, color.b, alpha);
         System.out.println(getOriginX());
         batch.draw(trc.get(entity).textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
      }
      // SnapshotArray<Actor> children = getChildren();
      // for (int i = 0; i < children.size; i++) {
      // children.items[i].draw(batch, alpha);
      // }
      super.draw(batch, parentAlpha);
   }

   // helpers

   public Vector2 getPositionOnStage() {
      Vector2 pos = new Vector2(getX(), getY());
      localToStageCoordinates(pos);
      return pos;
   }

   public float getRotationOnStage() {
      float sum = getRotation();
      Actor parent = getParent();
      while (parent != null) {
         sum += parent.getRotation();
         parent = parent.getParent();
      }
      return sum;
   }
}