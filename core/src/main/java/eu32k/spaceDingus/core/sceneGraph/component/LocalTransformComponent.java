package eu32k.spaceDingus.core.sceneGraph.component;

import com.artemis.Component;
import com.artemis.utils.Utils;

public class LocalTransformComponent extends Component {
   public float x;
   public float y;
   public float rotation;

   public LocalTransformComponent init() {
      x = 0;
      y = 0;
      rotation = 0;
      return this;
   }

   public LocalTransformComponent init(float x, float y) {
      this.x = x;
      this.y = y;
      return this;
   }

   public LocalTransformComponent init(float x, float y, float rotation) {
      init(x, y);
      this.rotation = rotation;
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
   }

   public void addRotation(float angle) {
      rotation = (rotation + angle) % 360;
   }

   public float getRotationAsRadians() {
      return (float) Math.toRadians(rotation);
   }

   public float getDistanceTo(LocalTransformComponent t) {
      return Utils.distance(t.getX(), t.getY(), x, y);
   }

}
