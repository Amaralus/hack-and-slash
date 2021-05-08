package amaralus.apps.hackandslash.gameplay;

import org.joml.Vector2f;

import static amaralus.apps.hackandslash.utils.VectMatrUtil.vec2;

public class PhysicService {

    private static final float LEFT_BORDER = -200;
    private static final float RIGHT_BORDER = 200;
    private static final float TOP_BORDER = -200;
    private static final float BOTTOM_BORDER = 200;

    public static Vector2f checkGlobalBorderCrossing(Vector2f position) {
        var crossingDistance = vec2();
        if (position.x < LEFT_BORDER)
            crossingDistance.x = position.x - LEFT_BORDER;
        else if (position.x > RIGHT_BORDER)
            crossingDistance.x = position.x - RIGHT_BORDER;

        if (position.y < TOP_BORDER)
            crossingDistance.y = position.y - TOP_BORDER;
        else if (position.y > BOTTOM_BORDER)
            crossingDistance.y = position.y - BOTTOM_BORDER;
        return crossingDistance;
    }
}
