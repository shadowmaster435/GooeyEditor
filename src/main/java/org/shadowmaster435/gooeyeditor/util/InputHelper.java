package org.shadowmaster435.gooeyeditor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InputHelper {

    public static boolean isLeftMouseHeld = false;
    public static boolean isRightMouseHeld = false;
    public static boolean isMiddleMouseHeld = false;
    public static boolean isShiftHeld = false;
    public static boolean isCtrlHeld = false;
    private static boolean callbackRegistered = false;

    public static void mouseCallback( long window, int button, int action, int mods) {

        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            updateMouse(button, action);
        }
    }



    public static void updateKeys() {
        isShiftHeld = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);
        isCtrlHeld = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL);

    }


    private static void updateMouse(int key, int action) {
        switch (key) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> {if (action == GLFW.GLFW_PRESS) isLeftMouseHeld = true; if (action == GLFW.GLFW_RELEASE) isLeftMouseHeld = false;}
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {if (action == GLFW.GLFW_PRESS) isRightMouseHeld = true; if (action == GLFW.GLFW_RELEASE) isRightMouseHeld = false;}
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> {if (action == GLFW.GLFW_PRESS) isMiddleMouseHeld = true; if (action == GLFW.GLFW_RELEASE) isMiddleMouseHeld = false;}
        }
    }




}
