package amaralus.apps.hackandslash.services.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public List<String> loadFileAsLinesFromResources(String path) {
        try {
            return readLinesFromInputStream(ClassLoader.getSystemResourceAsStream(path));
        } catch (Exception e) {
            throw new LoadFileException(e);
        }
    }

    public String loadFileAsStringFromResources(String path) {
        StringBuilder builder = new StringBuilder();
        loadFileAsLinesFromResources(path).forEach(line -> builder.append(line).append("\n"));
        return builder.toString();
    }

    private List<String> readLinesFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) list.add(line);
            return list;
        }
    }
}
