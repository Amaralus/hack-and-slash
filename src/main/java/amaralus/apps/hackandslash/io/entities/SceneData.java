package amaralus.apps.hackandslash.io.entities;

import java.util.List;

public class SceneData {

    private final CameraData camera;
    private final ResourcesData resources;
    private final List<EntityData> entities;

    public SceneData(CameraData camera, ResourcesData resources, List<EntityData> entities) {
        this.camera = camera;
        this.resources = resources;
        this.entities = entities;
    }

    public CameraData getCamera() {
        return camera;
    }

    public ResourcesData getResources() {
        return resources;
    }

    public List<EntityData> getEntities() {
        return entities;
    }
}
