package eu32k.gdx.artemis.extension;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.World;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.ParticleEffectComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;

public class EntityActor extends Group {
   private Entity entity;
   private ComponentMapper<TextureRegionComponent> trm;
   private ComponentMapper<ParticleEffectComponent> pem;
   private ComponentMapper<ActorComponent> ac;
   private Vector2 tempPosition;

   public EntityActor(World world, Entity entity) {
      this.entity = entity;
      trm = world.getMapper(TextureRegionComponent.class);
      pem = world.getMapper(ParticleEffectComponent.class);
      ac = world.getMapper(ActorComponent.class);
      tempPosition = new Vector2();
   }

   public Entity getEntity() {
      return entity;
   }

   @Override
   public void draw(SpriteBatch batch, float parentAlpha) {
      Color color = getColor();

      float alpha = color.a * parentAlpha;
      if (trm.has(entity) && trm.get(entity).show) {
         batch.setColor(color.r, color.g, color.b, alpha);
         batch.draw(trm.get(entity).textureRegion, getX() - getWidth() / 2.0f, getY() - getHeight() / 2.0f, getOriginX() + getWidth() / 2.0f, getOriginY() + getHeight() / 2.0f, getWidth(),
               getHeight(), getScaleX(), getScaleY(), getRotation());
      }
      if (pem.has(entity)) {
         if (ac.has(entity)) {
            EntityActor actor = ac.get(entity).actor;
            pem.get(entity).effect.setPosition(actor.getX(), actor.getY());
         } else {
            pem.get(entity).effect.setPosition(0, 0);
         }
         batch.setColor(color.r, color.g, color.b, alpha);
         pem.get(entity).effect.draw(batch);
      }
      super.draw(batch, parentAlpha);
   }

   // helpers

   public Vector2 getPositionOnStage() {
      tempPosition.set(0, 0);
      localToStageCoordinates(tempPosition);
      return tempPosition;
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