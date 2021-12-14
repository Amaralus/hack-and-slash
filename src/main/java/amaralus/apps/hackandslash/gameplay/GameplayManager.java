package amaralus.apps.hackandslash.gameplay;

import amaralus.apps.hackandslash.gameplay.entity.Entity;
import amaralus.apps.hackandslash.gameplay.entity.EntityFactory;
import amaralus.apps.hackandslash.gameplay.loop.GameLoop;
import amaralus.apps.hackandslash.gameplay.state.StateFactory;
import amaralus.apps.hackandslash.gameplay.state.action.InputEventProcessingAction;
import amaralus.apps.hackandslash.gameplay.state.action.MessageProcessingStateAction;
import amaralus.apps.hackandslash.graphics.Window;
import amaralus.apps.hackandslash.graphics.gpu.buffer.IntVertexBufferObject;
import amaralus.apps.hackandslash.graphics.gpu.texture.Texture;
import amaralus.apps.hackandslash.graphics.gpu.texture.TextureFactory;
import amaralus.apps.hackandslash.graphics.rendering.RendererService;
import amaralus.apps.hackandslash.graphics.sprites.Sprite;
import amaralus.apps.hackandslash.graphics.sprites.SpriteRenderComponent;
import amaralus.apps.hackandslash.io.FileLoadService;
import amaralus.apps.hackandslash.io.data.FrameStripData;
import amaralus.apps.hackandslash.io.data.SpriteSheetData;
import amaralus.apps.hackandslash.io.events.InputEventMessage;
import amaralus.apps.hackandslash.io.events.InputHandler;
import amaralus.apps.hackandslash.resources.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Collections;

import static amaralus.apps.hackandslash.common.message.SystemTopic.INPUT_TOPIC;
import static amaralus.apps.hackandslash.gameplay.CommandsPool.*;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.REMOVE;
import static amaralus.apps.hackandslash.gameplay.entity.EntityStatus.SLEEPING;
import static amaralus.apps.hackandslash.graphics.Color.CYAN;
import static amaralus.apps.hackandslash.graphics.Color.WHITE;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferType.ARRAY_BUFFER;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferUsage.DYNAMIC_DRAW;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.BufferUsage.STATIC_DRAW;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VaoFactory.newVao;
import static amaralus.apps.hackandslash.graphics.gpu.buffer.factory.VboFactory.floatBuffer;
import static amaralus.apps.hackandslash.graphics.gpu.texture.PixelFormat.RED;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureFilter.CLAMP_TO_EDGE;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureFilter.LINEAR;
import static amaralus.apps.hackandslash.graphics.gpu.texture.TextureParameterName.*;
import static amaralus.apps.hackandslash.graphics.scene.NodeRemovingStrategy.CASCADE;
import static amaralus.apps.hackandslash.io.events.keyboard.KeyCode.*;
import static amaralus.apps.hackandslash.io.events.mouse.MouseButton.MOUSE_BUTTON_LEFT;
import static amaralus.apps.hackandslash.io.events.mouse.MouseButton.MOUSE_BUTTON_RIGHT;
import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

@Service
public class GameplayManager {

    private final Window window;
    private final InputHandler inputHandler;
    private final EntityFactory entityFactory;
    private final GameLoop gameLoop;
    private final RendererService rendererService;

    private final FileLoadService fileLoadService;
    private final ResourceManager resourceManager;
    private final TextureFactory textureFactory;

    private Entity player;
    private Entity triangle;

    private STBTTFontinfo info;
    private STBTTBakedChar.Buffer cdata;
    private int ascent;
    private int descent;
    private int lineGap;

    public GameplayManager(Window window,
                           InputHandler inputHandler,
                           EntityFactory entityFactory,
                           GameLoop gameLoop,
                           RendererService rendererService,
                           FileLoadService fileLoadService,
                           ResourceManager resourceManager,
                           TextureFactory textureFactory) {
        this.window = window;
        this.inputHandler = inputHandler;
        this.entityFactory = entityFactory;
        this.gameLoop = gameLoop;
        this.rendererService = rendererService;
        this.fileLoadService = fileLoadService;
        this.resourceManager = resourceManager;
        this.textureFactory = textureFactory;
    }

    public void runGameLoop() {
        setUpInput();
//        setUpEntities();

        var fontSprite = initFont();

        entityFactory.entity()
                .renderComponent(new SpriteRenderComponent(fontSprite))
                .position(0, 200)
                .register();

        gameLoop.enable();
    }

    private Sprite initFont() {
        int width = 1024;
        int height = 1024;

        var ttf = loadFontBuffer("fonts/RobotoMonoRegular.ttf");

        initFontInfo(ttf);

        var texture = createFontTexture(ttf, width, height);

        var vao = newVao()
                .buffer(resourceManager.getResource("defaultTextureEbo", IntVertexBufferObject.class))
                .buffer(floatBuffer(FloatBuffer.allocate(16))
                        .type(ARRAY_BUFFER)
                        .usage(DYNAMIC_DRAW)
                        .saveAsVbo("font", resourceManager)
                        .dataFormat(0, 4, 4, 0, Float.TYPE))
                .saveAsVao("font", resourceManager)
                .build();

        var fontData = new FontData(texture, vao, info, cdata, ascent, descent, lineGap);
        rendererService.setFontData(fontData);

        var spriteSheetData = new SpriteSheetData(width, height, Collections.singletonList(new FrameStripData(1)));
        var spriteVao = newVao()
                .buffer(resourceManager.getResource("defaultTextureEbo", IntVertexBufferObject.class))
                .buffer(floatBuffer(textureData(texture, spriteSheetData))
                        .type(ARRAY_BUFFER)
                        .usage(STATIC_DRAW)
                        .saveAsVbo("fontSprite", resourceManager)
                        .dataFormat(0, 2, 2, 0, Float.TYPE))
                .saveAsVao("fontSprite", resourceManager)
                .build();


        return new Sprite("fontSprite", texture, spriteVao, spriteSheetData);
    }

    private float[] textureData(Texture texture, SpriteSheetData spriteSheetData) {
        var texturePosition = Sprite.frameTexturePosition(texture, spriteSheetData);
        return new float[]{0f, texturePosition.y, 0f, 0f, texturePosition.x, 0f, texturePosition.x, texturePosition.y};
    }

    private ByteBuffer loadFontBuffer(String path) {
        var fontBuffer = fileLoadService.loadFileAsByteBuffer(path);
        return BufferUtils.createByteBuffer(fontBuffer.capacity()).put(fontBuffer).flip();
    }

    private void initFontInfo(ByteBuffer ttf) {
        info = STBTTFontinfo.create();

        if (!stbtt_InitFont(info, ttf)) {
            throw new IllegalStateException("Failed to initialize font information.");
        }

        try (MemoryStack stack = stackPush()) {
            var pAscent = stack.mallocInt(1);
            var pDescent = stack.mallocInt(1);
            var pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }
    }

    private Texture createFontTexture(ByteBuffer ttf, int width, int height) {
        var pixels = BufferUtils.createByteBuffer(width * height);

        int characterBufferCapacity = 4092;
        float fontHeightInPixels = 64;

        cdata = STBTTBakedChar.malloc(characterBufferCapacity);
        stbtt_BakeFontBitmap(ttf, fontHeightInPixels, pixels, width, height, 32, cdata);

        var texture = textureFactory.newTexture("font")
                .width(width)
                .height(height)
                .pixels(pixels)
                .pixelFormat(RED)
                .generateMipmap()
                .param(WRAP_S, CLAMP_TO_EDGE)
                .param(WRAP_T, CLAMP_TO_EDGE)
                .param(MIN_FILTER, LINEAR)
                .param(MAG_FILTER, LINEAR)
                .produce();

        resourceManager.addResource(texture);
        return texture;
    }

    // -----------

    private void setUpInput() {
        inputHandler.singleAction(ESCAPE, window::close)
//                .buttonAction(W)
//                .buttonAction(S)
//                .buttonAction(A)
//                .buttonAction(D)
//                .singleAction(DIG1)
//                .singleAction(DIG2)
//                .singleAction(DIG3)
//                .singleAction(R)
//                .singleAction(MOUSE_BUTTON_LEFT)
//                .singleAction(MOUSE_BUTTON_RIGHT)
                .scrollAction((xOffset, yOffset) -> rendererService.getActiveScene().getCamera().addScale(yOffset));
    }

    private void setUpEntities() {

        setUpTriangle();

        setUpPlayer();

        entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("testTextureSheet")
                        .frameStrip(2)
                        .runAnimation()
                        .produce())
                .position(20, 20)
                .movementSpeed(200)
                .targetNode(triangle)
                .register();

        entityFactory.entity()
                .renderComponent(entityFactory.primitiveRenderComponent()
                        .line(vec2(-50, -50), vec2(50, 50))
                        .primitiveName("line")
                        .color(CYAN)
                        .produce())
                .entityStatus(SLEEPING)
                .register();
    }

    private void setUpPlayer() {
        player = entityFactory.entity()
                .renderComponent(entityFactory.spriteRenderComponent()
                        .spriteName("testTextureSheet")
                        .runAnimation()
                        .produce())
                .movementSpeed(200)
                .targetNode(triangle)
                .register();

        player.getMessageClient().subscribe(INPUT_TOPIC);

        player.setStateSystem(new StateFactory()
                .baseState("base", new MessageProcessingStateAction()
                        .onMessage(InputEventMessage.class, new InputEventProcessingAction()
                                .onButton(W, (stateContext, updateTime) -> ENTITY_MOVE_UP.execute(stateContext.entity()))
                                .onButton(S, (stateContext, updateTime) -> ENTITY_MOVE_DOWN.execute(stateContext.entity()))
                                .onButton(A, (stateContext, updateTime) -> ENTITY_MOVE_LEFT.execute(stateContext.entity()))
                                .onButton(D, (stateContext, updateTime) -> ENTITY_MOVE_RIGHT.execute(stateContext.entity()))
                                .onButton(DIG1, (stateContext, updateTime) -> changeFrameStrip(0).execute(stateContext.entity()))
                                .onButton(DIG2, (stateContext, updateTime) -> changeFrameStrip(1).execute(stateContext.entity()))
                                .onButton(DIG3, (stateContext, updateTime) -> changeFrameStrip(2).execute(stateContext.entity()))
                                .onButton(MOUSE_BUTTON_LEFT, (stateContext, updateTime) ->
                                        stateContext.entity().getPhysicalComponent().setPosition(rendererService.getGlobalCursorPosition()))
                        ))
                .produce());
    }

    private void setUpTriangle() {
        triangle = entityFactory.entity()
                .renderComponent(entityFactory.primitiveRenderComponent()
                        .triangle(vec2(0f, -40f), vec2(40f, 40f), vec2(-40f, 40f))
                        .primitiveName("triangle")
                        .color(WHITE)
                        .produce())
                .removingStrategy(CASCADE)
                .register();

        triangle.getMessageClient().subscribe(INPUT_TOPIC);

        triangle.setStateSystem(new StateFactory()
                .baseState("base", new MessageProcessingStateAction()
                        .onMessage(InputEventMessage.class, new InputEventProcessingAction()
                                .onButton(R, (stateContext, updateTime) -> stateContext.entity().setStatus(REMOVE))
                                .onButton(MOUSE_BUTTON_RIGHT, (stateContext, updateTime) ->
                                        stateContext.entity().getPhysicalComponent().setPosition(rendererService.getGlobalCursorPosition()))
                        ))
                .produce());
    }
}
