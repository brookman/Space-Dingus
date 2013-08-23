package eu32k.spaceDingus.core.sceneGraph;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;

import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;
import eu32k.spaceDingus.core.sceneGraph.system.SceneGraphSystem;

public class SceneGraphWorld extends World {

   private ComponentMapper<NodeComponent> nm;

   public SceneGraphWorld() {
      nm = getMapper(NodeComponent.class);
   }

   @Override
   public void addEntity(Entity e) {
      super.addEntity(e);
      if (!nm.has(e)) {
         return;
      }
      for (int i = 0; i < getSystems().size(); i++) {
         EntitySystem system = getSystems().get(i);
         if (system instanceof SceneGraphSystem) {
            ((SceneGraphSystem) system).entityAdded(e);
         }
      }
   }

   @Override
   public void deleteEntity(Entity e) {
      super.deleteEntity(e);
      if (!nm.has(e)) {
         return;
      }
      for (int i = 0; i < getSystems().size(); i++) {
         EntitySystem system = getSystems().get(i);
         if (system instanceof SceneGraphSystem) {
            ((SceneGraphSystem) system).entityDeleted(e);
         }
      }
   }
}
