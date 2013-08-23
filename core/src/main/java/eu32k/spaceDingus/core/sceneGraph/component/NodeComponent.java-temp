package eu32k.spaceDingus.core.sceneGraph.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;

public class NodeComponent extends Component {

   private Entity parent;
   private Bag<Entity> children;

   public NodeComponent init() {
      children = new Bag<Entity>();
      return this;
   }

   public NodeComponent init(Entity parent) {
      init();
      this.parent = parent;
      return this;
   }

   public Entity getParent() {
      return parent;
   }

   public Bag<Entity> getChildren() {
      return children;
   }

   public void addChild(Entity child) {
      children.add(child);
   }

   public void removeChild(Entity child) {
      children.remove(child);
   }

}
