package amaralus.apps.hackandslash.graphics.rendering;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.font.FontRenderComponent;
import amaralus.apps.hackandslash.graphics.gpu.shader.ShaderRepository;
import amaralus.apps.hackandslash.graphics.primitives.Primitive;
import amaralus.apps.hackandslash.graphics.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.scene.SceneManager;
import org.joml.Vector2f;
import org.springframework.stereotype.Service;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

@Service
public class RendererService {

    private final Window window;
    private final SpriteRenderer spriteRenderer;
    private final PrimitiveRenderer primitiveRenderer;
    private final FontRenderer fontRenderer;
    private final SceneManager sceneManager;


    public RendererService(Window window, PrimitiveRenderer primitiveRenderer, ShaderRepository shaderRepository, SceneManager sceneManager) {
        this.window = window;
        spriteRenderer = new SpriteRenderer(shaderRepository.get("texture"));
        this.primitiveRenderer = primitiveRenderer;
        fontRenderer = new FontRenderer(shaderRepository.get("font"));
        this.sceneManager = sceneManager;
    }

    public void render() {
        clear();

        var sceneLayers = sceneManager.getActiveScene().buildSceneGraphLayers();

        for (int i = sceneLayers.size() - 1; i >= 0; i--)
            for (var node : sceneLayers.get(i).getNodes())
                if (node instanceof Entity) {
                    var entity = (Entity) node;
                    var renderComponent = entity.getRenderComponent();

                    doRender(renderComponent, entity.getPhysicalComponent().getPosition());
                }

        window.swapBuffers();
    }

    private void doRender(RenderComponent renderComponent, Vector2f globalPosition) {
        if (renderComponent.isNull())
            return;

        var camera = sceneManager.getActiveScene().getCamera();

        switch (renderComponent.getRenderComponentType()) {
            case PRIMITIVE:
                primitiveRenderer.render(camera, renderComponent.wrapTo(Primitive.class), globalPosition);
                break;
            case SPRITE:
                spriteRenderer.render(camera, renderComponent.wrapTo(SpriteRenderComponent.class), globalPosition);
                break;
            case FONT:
                fontRenderer.renderText(camera, renderComponent.wrapTo(FontRenderComponent.class), globalPosition);
                break;
            default:
        }
    }

    private void clear() {
        var background = Color.WHITE;
        glClearColor(background.r(), background.g(), background.b(), background.a());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
