package amaralus.apps.hackandslash.scene;

import amaralus.apps.hackandslash.graphics.Color;
import lombok.Getter;
import lombok.Setter;

public class Scene {

    @Getter
    private final String name;
    @Getter
    private final Camera camera;
    @Getter
    private final SceneGraph sceneGraph;
    @Getter @Setter
    private Color backgroundColor;

    public Scene(String name, float width, float height) {
        this.name = name;
        camera = new Camera(width, height);
        camera.setScale(0.5f);
        sceneGraph = new SceneGraph();
        sceneGraph.addChildren(camera);
    }
}
