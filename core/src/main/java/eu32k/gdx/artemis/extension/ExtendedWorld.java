package eu32k.gdx.artemis.extension;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import eu32k.gdx.common.BodyBuilder;
import eu32k.gdx.common.FixtureDefBuilder;

public class ExtendedWorld extends eu32k.gdx.artemis.base.World {

   public World box2dWorld;
   public FixtureDefBuilder fdBuilder;
   public BodyBuilder bodyBuilder;

   public ExtendedWorld(World box2dWorld, Stage stage) {
      this.box2dWorld = box2dWorld;
      fdBuilder = new FixtureDefBuilder();
      bodyBuilder = new BodyBuilder(box2dWorld);
   }
}
