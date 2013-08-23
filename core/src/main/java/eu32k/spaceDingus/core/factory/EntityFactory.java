package eu32k.spaceDingus.core.factory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import eu32k.spaceDingus.core.common.BodyBuilder;
import eu32k.spaceDingus.core.common.FixtureDefBuilder;

public class EntityFactory {

   public static com.artemis.World artemisWorld;
   public static FixtureDefBuilder fdBuilder = new FixtureDefBuilder();
   public static BodyBuilder bodyBuilder;
   public static World box2dWorld;
   public static Stage stage;

   public static void init(com.artemis.World artemisWorld, World box2dWorld, AssetManager assets, Stage stage) {
      EntityFactory.artemisWorld = artemisWorld;
      EntityFactory.box2dWorld = box2dWorld;
      EntityFactory.stage = stage;
      bodyBuilder = new BodyBuilder(box2dWorld);
   }

}
