package eu32k.spaceDingus.core.component;

import com.artemis.Component;
import com.artemis.utils.Utils;
import com.badlogic.gdx.math.MathUtils;

import eu32k.spaceDingus.core.sceneGraph.component.LocalTransformComponent;

public class TransformComponent extends Component {

   public float x;
   public float y;
   private float rotation;

   public TransformComponent init() {
      x = 0;
      y = 0;
      rotation = 0;
      return this;
   }

   public TransformComponent init(float x, float y) {
      this.x = x;
      this.y = y;
      rotation = 0;
      return this;
   }

   public TransformComponent init(float x, float y, float rotation) {
      init(x, y);
      setRotation(rotation);
      return this;
   }

   public void addX(float x) {
      this.x += x;
   }

   public void addY(float y) {
      this.y += y;
   }

   public float getX() {
      return x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public void setLocation(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public float getRotation() {
      return rotation;
   }

   public void setRotation(float rotation) {
      this.rotation = rotation;
      while (this.rotation >= 360) {
         this.rotation -= 360;
      }
      while (this.rotation < 0) {
         this.rotation += 360;
      }
   }

   public void addRotation(float angle) {
      setRotation(rotation + angle);
   }

   public float getRotationAsRadians() {
      return MathUtils.degreesToRadians * rotation;
   }

   public float getDistanceTo(LocalTransformComponent t) {
      return Utils.distance(t.getX(), t.getY(), x, y);
   }

}
