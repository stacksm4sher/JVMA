package org.stacksm4sher.jvma;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class BytecodeUtils {

    private BytecodeUtils() {
    }

    public static RawBytecode parseBytecodeFromByteArrayAsRawWrapper(byte[] bytecode) {
        return new RawBytecode(bytecode);
    }

    public static RawBytecode parseBytecodeFromClassAsRawWrapper(Class<?> clazz) {
        byte[] bytecode = readBytecodeFromClass(clazz);
        return parseBytecodeFromByteArrayAsRawWrapper(bytecode);
    }

    public static ParsedBytecode parseBytecodeFromRawWrapper(RawBytecode rawBytecode) {
        return new ParsedBytecode(rawBytecode);
    }

    public static ParsedBytecode parseBytecodeFromByteArray(byte[] bytecode) {
        RawBytecode rawBytecode = parseBytecodeFromByteArrayAsRawWrapper(bytecode);
        return parseBytecodeFromRawWrapper(rawBytecode);
    }

    public static ParsedBytecode parseBytecodeFromClass(Class<?> clazz) {
        byte[] bytecode = readBytecodeFromClass(clazz);
        return parseBytecodeFromByteArray(bytecode);
    }

    private static byte[] readBytecodeFromClass(Class<?> clazz) {
        try {
            String resourceName = String.format("%s%s%s", "/", clazz.getName().replace(".", "/"), ".class");
            URL resource = clazz.getResource(resourceName);
            Path path = Paths.get(resource.toURI());
            return Files.readAllBytes(path);
        } catch (Exception exception) {
            throw new RuntimeException("", exception);
        }
    }

}
