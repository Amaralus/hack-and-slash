package amaralus.apps.hackandslash.io.data;

import java.nio.ByteBuffer;

public class ImageData {

    private final int height;
    private final int width;
    private final ByteBuffer imageBytes;

    public ImageData(int width, int height, ByteBuffer imageBytes) {
        this.width = width;
        this.height = height;
        this.imageBytes = imageBytes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getImageBytes() {
        return imageBytes;
    }
}
