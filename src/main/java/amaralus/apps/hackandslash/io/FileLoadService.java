package amaralus.apps.hackandslash.io;

import amaralus.apps.hackandslash.io.entities.ImageData;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileLoadService {

    public List<String> loadFileAsLines(String path) {
        try {
            return readLinesFromInputStream(loadResourceAsStream(path));
        } catch (Exception e) {
            throw new LoadFileException(e);
        }
    }

    public String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();
        loadFileAsLines(path).forEach(line -> builder.append(line).append("\n"));
        return builder.toString();
    }

    public ByteBuffer loadFileAsByteBuffer(String path) {
        try {
            return ByteBuffer.wrap(loadResourceAsStream(path).readAllBytes());
        } catch (Exception e) {
            throw new LoadFileException(e);
        }
    }

    public ImageData loadImageData(String path) {
        try {
            var image = ImageIO.read(loadResourceAsStream(path));

            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            var buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

            for (int y = 0; y < image.getHeight(); y++)
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            return new ImageData(image.getWidth(), image.getHeight(), buffer.flip());
        } catch (Exception e) {
            throw new LoadFileException(e);
        }
    }

    private List<String> readLinesFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) list.add(line);
            return list;
        }
    }

    protected InputStream loadResourceAsStream(String path) {
        return ClassLoader.getSystemResourceAsStream(path);
    }
}
