package com.troblecodings.tcutility.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troblecodings.tcutility.BlockProperties;
import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.init.TCBlocks;

public class FileReader {

    private static FileSystem fileSystemCache = null;

    public static Optional<Path> getRessourceLocation(String location) {

        final URL url = TCBlocks.class.getResource(location);
        try {
            if (url != null) {
                final URI uri = url.toURI();

                if ("file".equals(uri.getScheme())) {
                    if (!location.startsWith("/")) {
                        location = "/" + location;
                    }
                    final URL resource = TCBlocks.class.getResource(location);
                    if (resource == null) {
                        return Optional.empty();
                    }
                    return Optional.of(Paths.get(resource.toURI()));
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        return Optional.empty();
                    }
                    if (fileSystemCache == null) {
                        fileSystemCache = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    }
                    return Optional.of(fileSystemCache.getPath(location));

                }
            }
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
        return Optional.empty();
    }

    public static Map<String, String> readFiles(final String location) {
        final Map<String, String> files = new HashMap<>();
        final Optional<Path> loc = getRessourceLocation(location);
        if (loc.isPresent()) {
            final Path path = loc.get();
            try {
                final Stream<Path> inputs = Files.list(path);

                inputs.forEach(file -> {
                    try {
                        final String content = new String(Files.readAllBytes(file));
                        final String name = file.getFileName().toString();
                        files.put(name, content);
                    } catch (final IOException e) {
                        TCUtilityMain.LOG.warn("There was a problem during loading " + file + " !");
                        e.printStackTrace();
                    }
                });
                inputs.close();
                return files;
            } catch (final IOException e) {
                TCUtilityMain.LOG
                        .warn("There was a problem during listing all files from " + loc + " !");
                e.printStackTrace();
            }
        }
        if (files.isEmpty())
            TCUtilityMain.LOG.warn("No files found at " + location + "!");
        return files;
    }

    public static Map<String, BlockProperties> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final Map<String, String> entrySet = readFiles(directory);
        final Map<String, BlockProperties> properties = new HashMap<>();
        final Type typeOfHashMap = new TypeToken<Map<String, BlockProperties>>() {
        }.getType();
        if (entrySet != null) {
            entrySet.forEach((filename, content) -> {
                final Map<String, BlockProperties> json = gson.fromJson(content, typeOfHashMap);
                properties.putAll(json);
            });
        }
        return properties;
    }

}
