package org.shadowmaster435.gooeyeditor.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.FileOutputStream;

import static org.lwjgl.system.MemoryStack.stackPush;


/**
 * Opens a file dialogue. <p>
 * All processing is stopped until a choice is made.
 */
public final class SimpleFileDialogue {


    /**
     * Opens a file dialog to open a file.
     * @param fileExtension write in ".FILE" format (E.G. ".png")
     * @param fileDescriptor description for the file type (E.G. "Text Documents" or "Image Files")
     * @return selected file path or null if canceled.
     */
    public static String open(String fileExtension, String fileDescriptor, boolean allowMultiple) {
        return create(true, fileExtension, fileDescriptor, allowMultiple);
    }

    /**
     * Opens a file dialog to save a file.
     * @param fileExtension write in ".FILE" format (E.G. ".png")
     * @param fileDescriptor description for the file type (E.G. "Text Documents" or "Image Files")
     * @return selected file path or null if canceled.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save(String string, String fileExtension, String fileDescriptor) {
        var path = create(false, fileExtension, fileDescriptor, false);
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


    private static String create(boolean open, String fileExtension, String fileDescriptor, boolean allowMultiple) {
        if (open) {
            try (MemoryStack stack = stackPush()) {
                var filter = String.format("%1$s (*%2$s)", fileExtension, fileDescriptor);
                PointerBuffer aFilterPatterns = stack.mallocPointer(2);
                aFilterPatterns.put(stack.UTF8("*" + fileExtension));
                aFilterPatterns.flip();
                return TinyFileDialogs.tinyfd_openFileDialog("Open", "", aFilterPatterns, filter, allowMultiple);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "";
            }
        } else {
            try (MemoryStack stack = stackPush()) {
                var filter = String.format("%1$s (*%2$s)", fileExtension, fileDescriptor);
                PointerBuffer aFilterPatterns = stack.mallocPointer(2);
                aFilterPatterns.put(stack.UTF8("*" + fileExtension));
                aFilterPatterns.flip();
                return TinyFileDialogs.tinyfd_saveFileDialog("Save", "", aFilterPatterns, filter);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "";
            }
        }
    }
}