package amaralus.apps.hackandslash.io;

import amaralus.apps.hackandslash.io.data.ImageData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.BufferUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
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

    public <D> D loadFromJson(String path, Class<D> clazz) {
        try {
            return new Gson().fromJson(new InputStreamReader(loadResourceAsStream(path)), clazz);
        } catch (Exception e) {
            throw new LoadFileException(e);
        }
    }

    public List<String> loadFileNamesFromDirectory(String pathToDir) {
        try {
            var resource = ClassLoader.getSystemResource(pathToDir);

            if (resource == null)
                throw new FileNotFoundException(pathToDir);

            return Stream.of(Path.of(resource.toURI()).toFile().listFiles())
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
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

    protected InputStream loadResourceAsStream(String path) throws FileNotFoundException {
        log.debug("Загрузка файла: {}", path);
        InputStream stream = ClassLoader.getSystemResourceAsStream(path);

        if (stream == null)
            throw new FileNotFoundException(path);

        return stream;
    }
}
