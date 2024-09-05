package org.shadowmaster435.gooeyeditor.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.FileOutputStream;

import static org.lwjgl.system.MemoryStack.stackPush;

public final class SimpleFileDialogue {

    public static String open() {
        return create(true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save(String string) {
        var path = create(false);
        if (path == null) return;
        var file = new File(path);
        try {file.delete();} catch (Exception ignored) {}
        try {
            if (file.createNewFile()) {
                var stream = new FileOutputStream(file);
                stream.write(string.getBytes());
                stream.close();
            }
        } catch (Exception ignored) {}
    }


    private static String create(boolean open) {
        if (open) {
            try (MemoryStack stack = stackPush()) {
                var filter = "Json files (*.json)";
                PointerBuffer aFilterPatterns = stack.mallocPointer(2);
                aFilterPatterns.put(stack.UTF8("*.json"));
                aFilterPatterns.flip();
                return TinyFileDialogs.tinyfd_openFileDialog("Open", "", aFilterPatterns, filter, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "";
            }
        } else {
            try (MemoryStack stack = stackPush()) {
                var filter = "Json files (*.json)";
                PointerBuffer aFilterPatterns = stack.mallocPointer(2);
                aFilterPatterns.put(stack.UTF8("*.json"));
                aFilterPatterns.flip();
                return TinyFileDialogs.tinyfd_saveFileDialog("Save", "", aFilterPatterns, filter);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "";
            }
        }
    }
}