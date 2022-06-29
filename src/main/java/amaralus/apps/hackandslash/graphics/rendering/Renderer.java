package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.scene.Camera;
import org.joml.Vector2f;

public interface Renderer {

    void render(Camera camera, RenderComponent renderComponent, Vector2f position);

    RenderComponentType getRenderComponentType();
}
