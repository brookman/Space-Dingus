package eu32k.spaceDingus.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import eu32k.spaceDingus.core.SpaceDingus;

public class SpaceDingusHtml extends GwtApplication {
   @Override
   public ApplicationListener getApplicationListener() {
      return new SpaceDingus();
   }

   @Override
   public GwtApplicationConfiguration getConfig() {
      GwtApplicationConfiguration app = new GwtApplicationConfiguration(1000, 600);
      app.antialiasing = true;
      app.canvasId = "game-canvas";
      return app;
   }
}
