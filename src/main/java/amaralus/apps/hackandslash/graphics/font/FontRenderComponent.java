package amaralus.apps.hackandslash.graphics.font;

import amaralus.apps.hackandslash.graphics.Color;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponent;
import amaralus.apps.hackandslash.graphics.rendering.RenderComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static amaralus.apps.hackandslash.graphics.rendering.RenderComponentType.FONT;

@Getter
@Setter
@AllArgsConstructor
public class FontRenderComponent implements RenderComponent {

    private final Font font;
    private Color color;
    private String text;

    @Override
    public void update(long elapsedTime) {
        // do nothing
    }

    @Override
    public RenderComponentType getRenderComponentType() {
        return FONT;
    }
}
