package eu32k.spaceDingus.core.sceneGraph.system;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.Utils;

import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.sceneGraph.component.LocalTransformComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class SceneGraphSystem extends VoidEntitySystem {

   private ComponentMapper<NodeComponent> nodeMapper;
   private ComponentMapper<TransformComponent> transformMapper;
   private ComponentMapper<LocalTransformComponent> localTransformMapper;

   private Bag<Entity> rootNode;

   public SceneGraphSystem() {
      rootNode = new Bag<Entity>();
   }

   @Override
   protected void initialize() {
      nodeMapper = world.getMapper(NodeComponent.class);
      transformMapper = world.getMapper(TransformComponent.class);
      localTransformMapper = world.getMapper(LocalTransformComponent.class);
   }

   private void processEntity(Entity entity) {
      NodeComponent node = nodeMapper.get(entity);
      Bag<Entity> children = node.getChildren();

      for (int i = 0; i < children.size(); i++) {
         Entity e = children.get(i);
         updateWorldTransformation(e);
         processEntity(e);
      }
   }

   private void updateWorldTransformation(Entity e) {
      NodeComponent node = nodeMapper.get(e);
      Entity parent = node.getParent();
      TransformComponent parentTransform = transformMapper.get(parent);
      TransformComponent transform = transformMapper.get(e);
      LocalTransformComponent localTransform = localTransformMapper.get(e);

      float currentX = parentTransform.getX() + localTransform.getX();
      float currentY = parentTransform.getY() + localTransform.getY();

      float x = Utils.getRotatedX(currentX, currentY, parentTransform.getX(), parentTransform.getY(), parentTransform.getRotation());
      float y = Utils.getRotatedY(currentX, currentY, parentTransform.getX(), parentTransform.getY(), parentTransform.getRotation());

      transform.setLocation(x, y);
      transform.setRotation(parentTransform.getRotation() + localTransform.rotation);
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }

   @Override
   protected void removed(Entity e) {
   }

   public void entityAdded(Entity e) {
      NodeComponent node = nodeMapper.get(e);
      Entity parent = node.getParent();
      if (parent == null) {
         rootNode.add(e);
      } else {
         NodeComponent parentNode = nodeMapper.get(parent);
         parentNode.addChild(e);
      }

   }

   public void entityDeleted(Entity e) {
      NodeComponent node = nodeMapper.get(e);
      for (int i = 0; i < node.getChildren().size(); i++) {
         node.getChildren().get(i).deleteFromWorld();
      }
      rootNode.remove(e);
   }

   @Override
   protected void processSystem() {
      for (int i = 0; i < rootNode.size(); i++) {
         processEntity(rootNode.get(i));
      }
   }
}
