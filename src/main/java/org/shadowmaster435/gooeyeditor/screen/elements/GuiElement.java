package org.shadowmaster435.gooeyeditor.screen.elements;

//region Imports
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.VectorWidget;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;
import org.shadowmaster435.gooeyeditor.util.ClassCodeStringBuilder;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.shadowmaster435.gooeyeditor.util.VectorMath;

import java.lang.Math;
import java.lang.constant.Constable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
//endregion

// Class of nightmarish proportions.
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public abstract sealed class GuiElement implements Drawable, Selectable, Element, Widget permits ParentableWidgetBase {
    //region Fields
    //region floats
    public float scale_x = 1;
    public float scale_y = 1;
    public float rotation = 0;
    private float previous_rotation = 0;
    //endregion
    //region ints
    private int mouseRelClickX = 0;
    private int mouseRelClickY = 0;
    private int prevMouseX = 0;
    private int prevMouseY = 0;
    public int parent_offset_x = 0;
    public int parent_offset_y = 0;
    private int pre_transform_x = 0;
    private int pre_transform_y = 0;
    private int mouseClickX = 0;
    private int mouseClickY = 0;
    private int previous_x = 0;
    private int previous_y = 0;
    private int tooltip_x = 0;
    private int tooltip_y = 0;
    private int pre_transform_size_x = 0;
    private int pre_transform_size_y = 0;
    private int previous_size_x = 0;
    private int previous_size_y = 0;
    private int offset_x = 0;
    private int offset_y = 0;
    public int layer = 0;
    public int origin_x = 0;
    public int origin_y = 0;
    private final int resize_border_padding = 5;
    public GuiElement parent = null;
    //endregion
    //region bools
    public boolean center_screen = false;
    public boolean center_origin = false;
    public boolean offsetByParent = true;
    public boolean showsParentOffsetButton = true;
    public boolean needsExport = true;
    public boolean showChildren = true;

    public boolean selectable = true;
    private boolean editMode;
    public boolean selected = false;
    private boolean active = true;
    private boolean visible = true;
    private boolean was_moved = false;
    private boolean was_resized = false;
    private boolean offset = false;
    private boolean tooltipIsPositioned = false;
    private boolean scissor_enabled = false;
    private boolean dragging = false;
    private boolean resizing = false;
    private boolean rotating = false;
    private boolean focused = false;
    public boolean imported = false;
    //endregion
    //region misc
    private Rect2 scissor = new Rect2(0,0,0,0);
    private Rect2 pre_transform_rect = new Rect2(0,0,0,0);
    private ResizingType resizingType = ResizingType.NONE;
    private Property[] stored_properties;
    private Text tooltip = Text.of("");
    private Drawable childWidget;
    public String name = "";
    private Rect2 rect = new Rect2();
    //endregion
    //endregion
    //region Init
    public GuiElement(Drawable widget, int x, int y, boolean editMode) {
        this.editMode = editMode;
        this.rect.setBounds(x, y, 0, 0);
        this.childWidget = widget;
    }

    public GuiElement(int x, int y, boolean editMode) {
        this.rect.setBounds(x, y, 0, 0);
        this.editMode = editMode;
    }



    public GuiElement(int x, int y, int w, int h, boolean editMode) {
        this.rect.setBounds(x, y, w, h);
        this.editMode = editMode;
    }
    //endregion
    //region Built In Methods
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.mouseMoved(mouseX, mouseY);
            }
        }
        Element.super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.mouseClicked(mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.mouseReleased(mouseX, mouseY, button);
            }
        }
        return Element.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return Element.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            }
        }
        return Element.super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return Element.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.keyReleased(keyCode, scanCode, modifiers);
            }
        }
        return Element.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (childWidget != null) {
            if (childWidget instanceof Element element) {
                element.charTyped(chr, modifiers);
            }
        }
        return Element.super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return Element.super.getNavigationPath(navigation);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return getGlobalRect().contains(mouseX, mouseY) && active;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused && active;
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return Element.super.getFocusedPath();
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Element.super.getNavigationFocus();
    }

    @Override
    public void setX(int x) {
        if (offset) {
            if (x + offset_x == getX()) {
                return;
            }
            rect.x = x + offset_x ;
        } else {
            rect.x = x ;
        }
    }

    @Override
    public void setY(int y) {
        if (offset) {
            if (y + offset_y == getY()) {
                return;
            }

            rect.y = y + offset_y;
        } else {
            rect.y = y;
        }
    }

    public Vector2i getGlobalPosition() {
        return new Vector2i(getGlobalX(), getGlobalY());
    }

    public int getGlobalX() {
        return getX() + parent_offset_x;
    }

    public int getGlobalY() {
        return getY() + parent_offset_y;
    }

    @Override
    public int getX() {
        //return (offset) ? (int) rect.getX() + offset_x : (int) rect.getX();
        return (int) rect.getX();
    }

    @Override
    public int getY() {
        //return (offset) ? (int) rect.getY() + offset_y : (int) rect.getY();
        return (int) rect.getY();
    }

    @Override
    public int getWidth() {
        return rect.width;
    }

    @Override
    public int getHeight() {
        return rect.height;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        if (!visible) {
            return;
        }
        if (center_screen) {
            centerElement();
        }
        defaultRenderSequence(context, mouseX, mouseY, delta);
    }
    //endregion
    //region Getters, Setters, And Consumers.

    public void centerElement() {
        var dims = new Vector2i(MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
        if (parent != null) {
            dims = parent.getSize();
        }
        setX((dims.x / 2) - (getSize().x / 2));
        setY((dims.y / 2) - (getSize().y / 2));
    }

    public void forEachParent(Consumer<GuiElement> consumer) {
        var actualParent = parent;
        while (actualParent instanceof GuiElement element) {
            if (element.parent != null) {
                consumer.accept(element.parent);
            }
            actualParent = element.parent;
        }
    }


    public boolean isHoveringResizeBorder(int mouseX, int mouseY) {
        var inner_rect = new Rect2(getGlobalX() + resize_border_padding, getGlobalY() + resize_border_padding, getWidth() - resize_border_padding * 2, getHeight() - resize_border_padding * 2);
        var outer_rect = new Rect2(getGlobalX(), getGlobalY(), getWidth(), getHeight());
        return outer_rect.contains(mouseX, mouseY) && !inner_rect.contains(mouseX, mouseY);
    }

    public void setEditMode(boolean value) {
        editMode = value;
    }

    public void setScissor(int x1, int y1, int x2, int y2) {
        scissor = new Rect2(new Vector2i(x1, y1), new Vector2i(x2, y2));
    }


    public void positionToolTip(boolean value) {
        this.tooltipIsPositioned = true;
    }

    public void setTooltipPos(int x, int y) {
        tooltip_x = x;
        tooltip_y = y;
    }

    public void setTooltipX(int tooltip_x) {
        this.tooltip_x = tooltip_x;
    }

    public void setTooltipY(int tooltip_y) {
        this.tooltip_y = tooltip_y;
    }

    public int getTooltipX() {
        return tooltip_x;
    }

    public int getTooltipY() {
        return tooltip_y;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = Text.of(tooltip);
    }
    public void setTooltip(Text tooltip) {
        this.tooltip = tooltip;
    }

    public Text getTooltip() {
        return tooltip;
    }

    public boolean moved() {
        return was_moved;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isActive() {
        return active;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setWidth(int w) {
        rect.width = w;
    }

    public void setHeight(int h) {
        rect.height = h;
    }

    public Rect2 getRect() {
        return this.rect;
    }

    public void offset(boolean val) {
        this.offset = val;
    }

    public boolean isOffset() {
        return offset;
    }

    public void scissor(boolean val) {
        this.scissor_enabled = val;
    }

    public boolean resized() {
        return was_resized;
    }

    public boolean changed() {
        return moved() || resized();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCursorType(int mouseX, int mouseY) {
        if (!editMode) {
            return;
        }
        var type = getResizingType(mouseX, mouseY);
        if (isMouseOver(mouseX, mouseY)) {

            if (!selected) {
                GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR));
            } else {
                if (InputHelper.isShiftHeld && !InputHelper.isLeftAltHeld) {
                    GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(type.rotate_cursor));
                } else if (!InputHelper.isLeftAltHeld) {
                    GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(type.cursor));
                } else {
                    GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_ALL_CURSOR));
                }
            }

        } else {
            GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
        }
    }

    public Rect2 getGlobalRect() {
        return new Rect2(getGlobalX(), getGlobalY(), getWidth(), getHeight());
    }

    public int getOffsetX() {
        return offset_x;
    }

    public void setOffsetX(int offset_x) {
        this.offset_x = offset_x;
    }

    public int getOffsetY() {
        return offset_y;
    }

    public void setOffsetY(int offset_y) {
        this.offset_y = -offset_y;
    }

    public int getMouseRelClickX() {
        return mouseRelClickX;
    }

    public int getMouseRelClickY() {
        return mouseRelClickY;
    }

    public ResizingType getResizingType(int mouseX, int mouseY) {
        return ResizingType.fromPatchRegion(pre_transform_rect.getPointPatchRegion(mouseX, mouseY, resize_border_padding));
    }

    public void setSize(int w, int h) {
        setWidth(w);
        setHeight(h);
    }
    
    public void setSize(Vector2i size) {
        setWidth(size.x);
        setHeight(size.y);
    }
    public void setScale(float x, float y) {
        this.scale_x = x;
        this.scale_y = y;
    }
    public void setScale(Vector2f scale) {
        this.scale_x = scale.x;
        this.scale_y = scale.y;
    }

    public void setPosition(Vector2i position) {
        setX(position.x);
        setY(position.y);
    }

    public void setOrigin(int x, int y) {
        origin_x = x;
        origin_y = y;
    }
    
    public void setOrigin(Vector2i origin) {
        origin_x = origin.x;
        origin_y = origin.y;
    }

    public Vector2i getPosition() {
        return new Vector2i(getX(), getY());
    }

    public Vector2i getSize() {
        return new Vector2i(getWidth(), getHeight());
    }

    public Vector2f getScale() {
        return new Vector2f(scale_x, scale_y);
    }

    public Vector2i getOrigin() {
        return new Vector2i(origin_x, origin_y);
    }

    public Drawable getChildWidget() {
        return childWidget;
    }
    //endregion
    //region Enums
    public enum ResizingType {
        NONE(0, 0, GLFW.GLFW_RESIZE_ALL_CURSOR, GLFW.GLFW_RESIZE_ALL_CURSOR),
        TOP(0, -1, GLFW.GLFW_RESIZE_NS_CURSOR, GLFW.GLFW_RESIZE_EW_CURSOR),
        BOTTOM(0, 1, GLFW.GLFW_RESIZE_NS_CURSOR, GLFW.GLFW_RESIZE_EW_CURSOR),
        LEFT(-1, 0, GLFW.GLFW_RESIZE_EW_CURSOR, GLFW.GLFW_RESIZE_NS_CURSOR),
        RIGHT(1, 0, GLFW.GLFW_RESIZE_EW_CURSOR, GLFW.GLFW_RESIZE_NS_CURSOR),
        TL(-1, -1, GLFW.GLFW_RESIZE_NWSE_CURSOR, GLFW.GLFW_RESIZE_NESW_CURSOR),
        TR(1, -1, GLFW.GLFW_RESIZE_NESW_CURSOR, GLFW.GLFW_RESIZE_NWSE_CURSOR),
        BL(-1, 1, GLFW.GLFW_RESIZE_NESW_CURSOR, GLFW.GLFW_RESIZE_NWSE_CURSOR),
        BR(1, 1, GLFW.GLFW_RESIZE_NWSE_CURSOR, GLFW.GLFW_RESIZE_NESW_CURSOR);

        final int x;
        final int y;
        final int cursor;
        final int rotate_cursor;

        ResizingType(int x, int y, int cursor, int rotate_cursor) {
            this.x = x;
            this.y = y;
            this.cursor = cursor;
            this.rotate_cursor = rotate_cursor;
        }

        public static ResizingType fromPatchRegion(Rect2.NinePatchRegion type) {
            var result = ResizingType.NONE;
            switch (type) {
                case TR -> result = ResizingType.TR;
                case TL -> result = ResizingType.TL;
                case BR -> result = ResizingType.BR;
                case BL -> result = ResizingType.BL;
                case TOP -> result = ResizingType.TOP;
                case BOTTOM -> result = ResizingType.BOTTOM;
                case LEFT -> result = ResizingType.LEFT;
                case RIGHT -> result = ResizingType.RIGHT;

            }
            return result;
        }

        public Rect2.NinePatchRegion toPatchRegion() {
            var result = Rect2.NinePatchRegion.NONE;
            switch (this) {
                case TR -> result = Rect2.NinePatchRegion.TR;
                case TL -> result = Rect2.NinePatchRegion.TL;
                case BR -> result = Rect2.NinePatchRegion.BR;
                case BL -> result = Rect2.NinePatchRegion.BL;
                case TOP -> result = Rect2.NinePatchRegion.TOP;
                case BOTTOM -> result = Rect2.NinePatchRegion.BOTTOM;
                case LEFT -> result = Rect2.NinePatchRegion.LEFT;
                case RIGHT -> result = Rect2.NinePatchRegion.RIGHT;

            }
            return result;
        }
    }
    //endregion
    //region Editor Property Stuff


    public HashMap<Property, ?> getCurrentPropertyValueMap() {
        HashMap<Property, ?> result = new HashMap<>();
        var props = stored_properties;
        for (Property property : props) {
            result.put(property, property.get(this));
        }
        return result;
    }

    public final void storeProperties() {
        stored_properties = loadProperties();
    }

    public final Property[] loadProperties() {
        ArrayList<Property> properties = new ArrayList<>(Arrays.stream(getProperties()).toList());
        ArrayList<Property> default_properties = new ArrayList<>(Arrays.stream(getDefaultProperties()).toList());
        default_properties.addAll(properties);
        return default_properties.toArray(new Property[]{});
    }

    public final Property[] getDefaultProperties() {
        var name = new Property("Name", "name", "name", String.class);
        var pos = new Property("Position", "setPosition", "getPosition", Vector2i.class);
        var size = new Property("Size", "setSize", "getSize", Vector2i.class);
        var scale = new Property("Scale", "setScale", "getScale", Vector2f.class);
        var origin = new Property("Origin", "setOrigin", "getOrigin", Vector2i.class);
        var center_origin = new Property("Center Origin", "center_origin", "center_origin", Boolean.class);
        var center_screen = new Property("Center", "center_screen", "center_screen", Boolean.class);
        var offsetByParent = new Property("Localize Position", "offsetByParent", "offsetByParent", Boolean.class);
        var rotation = new Property("Rotation", "rotation", "rotation", Float.class);
        var layer = new Property("Layer", "layer", "layer", Integer.class);
        return new Property[]{name, pos, size, scale, rotation, layer, origin, center_origin, center_screen};
    }

    /**
     * Return an array of properties (see {@link Property} and {@link GuiElement#getDefaultProperties()}) <p>
     * to be used by the editor to generate input fields.
     */
    public abstract Property[] getProperties();
    /**
     * @param properties Arrays to merge
     * @return a merged array of any amount of property arrays.
     */
    public Property[] mergeProperties(Property[]... properties) {
        ArrayList<Property> list = new ArrayList<>();
        for (Property[] props : properties) {
            list.addAll(Arrays.stream(props).toList());
        }
        return list.toArray(new Property[]{});
    }
    /**
     * @param first Array of properties to merge.
     * @param props Properties to merge with {@code first}.
     * @return a merged array of any amount of property arrays.
     */
    public Property[] mergeProperties(Property[] first, Property... props) {
        ArrayList<Property> list = new ArrayList<>(Arrays.stream(first).toList());
        list.addAll(Arrays.asList(props));
        return list.toArray(new Property[]{});
    }
    /**
     * @param props Properties to merge.
     * @return a merged array of any amount of property arrays.
     */
    public Property[] mergeProperties(Property... props) {
        return new ArrayList<>(Arrays.asList(props)).toArray(new Property[]{});
    }

    private static final Map<Class<?>, String> classMap = Map.ofEntries(
            Map.entry(Boolean.class, "bool"),
            Map.entry(String.class, "str"),
            Map.entry(Double.class, "double"),
            Map.entry(Float.class, "float"),
            Map.entry(Integer.class, "int"),
            Map.entry(Identifier.class, "id"),
            Map.entry(Vector2i.class, "vec2i"),
            Map.entry(Vector2d.class, "vec2d"),
            Map.entry(Vector2f.class, "vec2f"),
            Map.entry(Vector3i.class, "vec3i"),
            Map.entry(Vector3d.class, "vec3d"),
            Map.entry(Vector3f.class, "vec3f"),
            Map.entry(Vector4i.class, "vec4i"),
            Map.entry(Vector4d.class, "vec4d"),
            Map.entry(Vector4f.class, "vec4f")
    );


    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "ConfusingArgumentToVarargsMethod"})
    public record Property(String display_name, String setterMethodOrFieldName, String getterMethodOrFieldName, Class<?> aClass) {

        public <O> boolean isMethodSetter(O object) {
            boolean is_method = false;
            try {
                // throws if field doesn't exist
                tryGetField(object, true).getClass();
            } catch (Exception e) {

                is_method = true;
            }
            return is_method;
        }

        public static  <V> Pair<Property, ?> fromJson(JsonObject jsonObject) {
            var display_name = jsonObject.get("display_name").getAsString();
            var setter = jsonObject.get("setterMethodOrFieldName").getAsString();
            var getter = jsonObject.get("getterMethodOrFieldName").getAsString();
            var type = readClassType(jsonObject.get("type").getAsString());
            JsonElement element = jsonObject.get("value");
            V value;
            if (element instanceof JsonObject object) {
                value = (V) Identifier.of(object.get("path").getAsString(), object.get("namespace").getAsString());
            } else if (element instanceof JsonArray) {
                value = readVec(jsonObject);
            } else {
                value = readPrimitive(jsonObject);
            }
            return new Pair<>(new Property(display_name, setter, getter, type), value);
        }

        public void writeJson(JsonObject newJObj, Object objInstance) {
            var type = classMap.get(aClass);
            newJObj.addProperty("display_name", display_name);
            newJObj.addProperty("setterMethodOrFieldName", setterMethodOrFieldName);
            newJObj.addProperty("getterMethodOrFieldName", getterMethodOrFieldName);
            newJObj.addProperty("type", type);
            var val = get(objInstance);
            if (val instanceof Identifier id) {
                var idObj = new JsonObject();
                idObj.addProperty("namespace", id.getPath());
                idObj.addProperty("path", id.getNamespace());
                newJObj.add("value", idObj);
            } else if (isVec(aClass)) {
                writeVec(newJObj, val);
            } else {
                writePrimitive(newJObj, type, val);
            }
        }

        public static  <V> V readPrimitive(JsonObject obj) {
            var type = obj.get("type").getAsString();
            Object result = null;
            switch (type) {
                case "str" -> result = obj.get("value").getAsString();
                case "int" -> result = obj.get("value").getAsInt();
                case "float" -> result = obj.get("value").getAsFloat();
                case "double" -> result = obj.get("value").getAsDouble();
                case "bool" -> result = obj.get("value").getAsBoolean();
            }
            return (V) result;
        }

        public static void writePrimitive(JsonObject obj, String type, Object val) {
            switch (type) {
                case "str" -> obj.addProperty("value", (String) val);
                case "int" -> obj.addProperty("value", (Integer) val);
                case "float" -> obj.addProperty("value", (Float) val);
                case "double" -> obj.addProperty("value", (Double) val);
                case "bool" -> obj.addProperty("value", (Boolean) val);
            }
        }

        public static void writeVec(JsonObject obj, Object val) {
            var nums = VectorWidget.convertVector(val);
            var arr = new JsonArray();
            for (Number number : nums) {
                arr.add(number);
            }
            obj.add("value", arr);
        }



        public static  <V> V readVec(JsonObject obj) {
            var type = obj.get("type").getAsString();
            var nums = obj.get("value").getAsJsonArray();
            if (type.endsWith("i")) {
                ArrayList<Integer> numList = new ArrayList<>();
                for (JsonElement element : nums) {
                    numList.add(element.getAsInt());
                }
                try {
                    Integer[] values = numList.toArray(new Integer[0]);
                    Class<?>[] classes = new Class<?>[numList.size()];
                    Arrays.fill(classes, int.class);
                    return (V) readClassType(type).getConstructor(classes).newInstance(values);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (type.endsWith("f")) {
                ArrayList<Float> numList = new ArrayList<>();
                for (JsonElement element : nums) {
                    numList.add(element.getAsFloat());
                }
                try {
                    Float[] values = numList.toArray(new Float[0]);
                    Class<?>[] classes = new Class<?>[numList.size()];
                    Arrays.fill(classes, float.class);
                    return (V) readClassType(type).getConstructor(classes).newInstance(values);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (type.endsWith("d")) {
                ArrayList<Double> numList = new ArrayList<>();
                for (JsonElement element : nums) {
                    numList.add(element.getAsDouble());
                }
                try {
                    Double[] values = numList.toArray(new Double[0]);
                    Class<?>[] classes = new Class<?>[numList.size()];
                    Arrays.fill(classes, double.class);
                    return (V) readClassType(type).getConstructor(classes).newInstance(values);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }


        private static Class<?> readClassType(String type) {
            Class<?> clazz = null;
            switch (type) {
                case "vec2i" -> clazz = Vector2i.class;
                case "vec2d" -> clazz = Vector2d.class;
                case "vec2f" -> clazz = Vector2f.class;
                case "vec3i" -> clazz = Vector3i.class;
                case "vec3d" -> clazz = Vector3d.class;
                case "vec3f" -> clazz = Vector3f.class;
                case "vec4i" -> clazz = Vector4i.class;
                case "vec4d" -> clazz = Vector4d.class;
                case "vec4f" -> clazz = Vector4f.class;
                case "id" -> clazz = Identifier.class;
                case "int" -> clazz = Constable.class;
                case "double" -> clazz = Double.class;
                case "float" -> clazz = Float.class;
                case "str" -> clazz = String.class;
                case "bool" -> clazz = Boolean.class;
            }
            return clazz;
        }

        private static  <V> boolean isVec(Class<?> obj) {
            return ClassCodeStringBuilder.getSimpleCanonicalName(obj).startsWith("Vector");
        }


        public  <O, T> T get(O object) {
            boolean is_method = false;
            try {
                // throws if field doesn't exist
                tryGetField(object, true).getClass();
            } catch (Exception e) {

                is_method = true;
            }
            if (is_method) {
                try {
                    return (T) tryGetMethod(object, true).invoke(object);

                } catch (Exception e) {

                    return null;
                }
            } else {
                try {
                    return (T) tryGetField(object, true).get(object);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public <O, T> void set(O object, T... inputs) {
            boolean is_method = false;

            if (inputs.length > 1) {
                // can't set a field with more than one input
                is_method = true;
            } else {
                try {
                    // throws if field doesn't exist
                    tryGetField(object, false).getClass();
                } catch (Exception e) {

                    is_method = true;
                }
            }
            if (is_method) {
                try {
                    //noinspection ConfusingArgumentToVarargsMethod
                    tryGetMethod(object, false, inputs).invoke(object, inputs);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    tryGetField(object, false).set(object, inputs[0]);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private <O> Field tryGetField(O object, boolean getter) {
            String methodOrFieldName = (getter) ? getterMethodOrFieldName : setterMethodOrFieldName;
            try {
                return object.getClass().getField(methodOrFieldName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private <O, T> Method tryGetMethod(O object, boolean getter, T... inputs) {
            String methodOrFieldName = (getter) ? getterMethodOrFieldName : setterMethodOrFieldName;

            try {
                ArrayList<Class<?>> input_classes = new ArrayList<>();
                Arrays.stream(inputs).toList().forEach(c -> {
                    if (c instanceof Integer) {
                        input_classes.add(int.class);
                    } else if (c instanceof Float) {
                        input_classes.add(float.class);

                    }
                    else if (c instanceof Double) {
                        input_classes.add(double.class);

                    }
                    else if (c instanceof Boolean) {
                        input_classes.add(boolean.class);

                    } else {
                        input_classes.add(c.getClass());
                    }
                });
                return object.getClass().getMethod(methodOrFieldName, input_classes.toArray(new Class<?>[]{}));
            }  catch (Exception e) {
                throw new RuntimeException(e);

            }
        }
    }
    //endregion
    //region Draw Functions
    public void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (!tooltip.getString().isEmpty() && isMouseOver(mouseX, mouseY)) {
            if (tooltipIsPositioned) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, tooltip_x, tooltip_y);
            } else {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, mouseX, mouseY);
            }
        }
    }

    public void renderBlock(DrawContext context, BlockState state, float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ) {
        var manager = MinecraftClient.getInstance().getBlockRenderManager();
        var matrices = context.getMatrices();
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationZ));
        matrices.translate(offsetX, offsetY, offsetZ);
        manager.renderBlockAsEntity(state, matrices, context.getVertexConsumers(), 15728880, 15728880);
        matrices.pop();

    }

    public final void defaultRenderSequence(DrawContext context, int mouseX, int mouseY, float delta) {
        updatePrevious(mouseX, mouseY);
        boolean safety_scissors = false; // haha. prevents an underflow.
        push(context);
        if (scissor_enabled) {
            safety_scissors = true;
            context.enableScissor(scissor.x, scissor.y , scissor.width, scissor.height);
        }
        applyTransforms(context, mouseX, mouseY, delta);
        preTransform(context, mouseX, mouseY, delta);
        if (scissor_enabled && safety_scissors) {
            context.disableScissor();
        }
        renderTooltip(context, mouseX, mouseY);
        postTransform(context, mouseX, mouseY, delta);
        pop(context);

        drawSelectionBox(context);
        applyPosRotScale(mouseX, mouseY);
        was_moved = getX() != previous_x || getY() != previous_y;
        previous_x = getX();
        previous_y = getY();
        was_resized = getWidth() != previous_x || getHeight() != previous_y;
        previous_x = getWidth();
        previous_y = getHeight();
    }

    public void drawText(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        var textRenderer = MinecraftClient.getInstance().textRenderer;

        var endY = y + textRenderer.fontHeight;
        var endX = x + textRenderer.getWidth(text);

        int i = textRenderer.getWidth(text);
        int j = (y + endY - 9) / 2 + 1;
        int k = endX - x;
        int l = i - k;
        double d = (double) Util.getMeasuringTimeMs() / 1000.0;
        double e = Math.max((double)l * 0.5, 3.0);
        double f = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d / e)) / 2.0 + 0.5;
        double g = MathHelper.lerp(f, 0.0, l);
        context.enableScissor(x, y, endX, endY);
        context.drawText(textRenderer, text, x - (int)g, j, color, shadow);
        context.disableScissor();
    }


    /**
     * Anything drawn here will be affected by position rotation, scale, and scissor. <p>
     * This is where you will most likely be doing most of your rendering.
     */
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {}

    /**
     * Anything drawn here will NOT be affected by position rotation, scale, and scissor.
     */
    public void postTransform(DrawContext context, int mouseX, int mouseY, float delta) {}

    private void updatePrevious(int mouseX, int mouseY) {
        layer = Math.max(layer, 1);
        if (prevMouseX != mouseX || prevMouseY != mouseY) {
            mouseMoved(mouseX, mouseY);
        }
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    public void drawSelectionBox(DrawContext context) {
        if (selected) {
            context.fill(getGlobalX(), getGlobalY(), getGlobalX() + getWidth(), getGlobalY() + getHeight(), layer - 1, ColorHelper.Argb.getArgb(100,255,255,255));
        }
    }

    private void applyTransforms(DrawContext context, int mouseX, int mouseY, float delta) {
        var origin_x = (center_origin) ? this.origin_x + getWidth() / 2 : this.origin_x;
        var origin_y = (center_origin) ? this.origin_y + getHeight() / 2 : this.origin_y;
        if (childWidget != null) {
            var widget = (Widget) childWidget;

            context.getMatrices().translate(getGlobalX() + origin_x, getGlobalY() + origin_y, 0);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
            context.getMatrices().scale(scale_x, scale_y, 1);

            context.getMatrices().translate(-widget.getX() - origin_x, -widget.getY() - origin_y, layer);

            childWidget.render(context, mouseX, mouseY, delta);
        } else  {
            context.getMatrices().translate(origin_x + getGlobalX(), origin_y + getGlobalY(), layer);
            context.getMatrices().scale(scale_x, scale_y, 1);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
            context.getMatrices().translate(-getGlobalX() - origin_x, -getGlobalY() - origin_y, layer);

        }
        preTransform(context, mouseX, mouseY, delta);

    }

    private void applyPosRotScale(int mouseX, int mouseY) {
        if ((resizing || dragging || rotating)) {
            if (InputHelper.isLeftMouseHeld) {
                transform(mouseX, mouseY);
            } else {
                stopTransform();
            }
        }
    }

    public void pop(DrawContext context) {
        context.getMatrices().pop();
    }
    public void push(DrawContext context) {
        context.getMatrices().push();
    }
    public void transform(int mouseX, int mouseY) {
        if (editMode) {
            if (resizing) {
                var type = resizingType;
                if (type.x < 0) {
                    setX(mouseX - mouseRelClickX);
                }
                if (type.y < 0) {
                    setY(mouseY - mouseRelClickY);
                }
                var rect = Rect2.fromPoints(new Vector2i(mouseX, mouseY), new Vector2i(pre_transform_x + pre_transform_size_x, pre_transform_y + pre_transform_size_y));
                if (type.x < 0) {
                    setWidth(rect.width);
                }
                if (type.x > 0) {
                    setWidth(Math.abs(getGlobalX() - mouseX) + Math.abs((mouseRelClickX - parent_offset_x) - pre_transform_size_x));
                }
                if (type.y < 0) {
                    setHeight(rect.height);
                }
                if (type.y > 0) {
                    setHeight(Math.abs(getGlobalY() - mouseY) + Math.abs((mouseRelClickY - parent_offset_y) - pre_transform_size_y));
                }
            }
            else if (dragging) {
                setX(mouseX - mouseRelClickX);
                setY(mouseY - mouseRelClickY);
            } else if (rotating) {
                var ang = VectorMath.angleToPoint(new Vector2f(mouseX,mouseY), new Vector2f(mouseClickX, mouseClickY));
                rotation = (float) Math.toDegrees(ang);
            }
        }
    }

    public void stopTransform() {
        if (editMode) {
            resizingType = ResizingType.NONE;
            dragging = false;
            resizing = false;
            rotating = false;
        }
    }

    public void startTransform(int mouseX, int mouseY) {
        if (editMode && isMouseOver(mouseX, mouseY)) {
            if (isHoveringResizeBorder(mouseX, mouseY) && !InputHelper.isLeftAltHeld) {
                if (InputHelper.isShiftHeld) {
                    rotating = true;
                    previous_rotation = rotation;
                } else {
                    resizing = true;
                    resizingType = getResizingType(mouseX, mouseY);
                    pre_transform_size_x = getWidth();
                    pre_transform_size_y = getHeight();
                }

            } else {
                dragging = true;
            }
            pre_transform_x = getGlobalX();
            pre_transform_y = getGlobalY();
            pre_transform_rect = getGlobalRect();
            mouseClickX = mouseX;
            mouseClickY = mouseY;
            mouseRelClickX = Math.abs(mouseX - getX());
            mouseRelClickY = Math.abs(mouseY - getY());

        }
    }
    public void drawItem(DrawContext context, ItemStack stack, int x, int y, int w, int h) { // minecraft's gui block model rendering code makes me want to die
        if (!stack.isEmpty()) {
            var matrices = context.getMatrices();
            BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
            matrices.push();

            if (stack.getCount() != 1) {
                context.getMatrices().push();
                String string = String.valueOf(stack.getCount());

                context.getMatrices().translate(x ,y - h, layer);

                context.getMatrices().scale(w / 16f, h / 16f, (float) getSize().length());
                context.getMatrices().translate(-w, -h, 1.0F);

                context.drawText(MinecraftClient.getInstance().textRenderer, String.valueOf(stack.getCount()), w - MinecraftClient.getInstance().textRenderer.getWidth(string) + 16, (h - 10) + 35, 16777215, true);
                context.getMatrices().pop();
            }

            matrices.translate(x + (w / 2f), y + (h / 2f), layer + ((float) (layer + getSize().length() / 4f)));
            matrices.scale(w, -h, (float) getSize().length());
            try {
                boolean bl = !bakedModel.isSideLit();

                if (bl) {

                    DiffuseLighting.disableGuiDepthLighting();

                }
                MinecraftClient.getInstance()
                        .getItemRenderer()
                        .renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
                // I love how lighting stops working properly when you scale a model up :D
                context.draw();
                if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable var12) {
                CrashReport crashReport = CrashReport.create(var12, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }

            matrices.pop();
        }
    }
    public void drawNinePatchTexture(DrawContext context, Rect2 rect, NinePatchTextureData data, int edge_thickness, int texture_width, int texture_height) {
        var texture = data.texture();
        drawEdges(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCenter(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCorners(context, rect, texture, edge_thickness, texture_width, texture_height);
    }

    public void drawNinePatchTexture(DrawContext context, NinePatchTextureData data) {
        drawNinePatchTexture(context, this.getGlobalRect(), data);
    }

    public void drawNinePatchTexture(DrawContext context, Rect2 rect, NinePatchTextureData data) {
        var texture = data.texture();
        var texture_width = data.texture_width();
        var texture_height = data.texture_height();
        var edge_thickness = data.edge_thickness();
        drawEdges(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCenter(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCorners(context, rect, texture, edge_thickness, texture_width, texture_height);
    }

    public void drawNinePatchTexture(DrawContext context, Rect2 rect, Identifier texture, int edge_thickness, int texture_width, int texture_height) {
        drawEdges(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCenter(context, rect, texture, edge_thickness, texture_width, texture_height);
        drawCorners(context, rect, texture, edge_thickness, texture_width, texture_height);
    }


    /**
     * Draws a texture with the supplied shader over the entire element.
     */
    public void drawShaderTexture(DrawContext context, Identifier texture, int x, int y, Supplier<ShaderProgram> shader) {
        var width = getWidth();
        var height = getHeight();
        RenderSystem.setShader(shader);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.disableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float) layer).texture(0, 0).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y + height, (float)layer).texture(0, 1).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)layer).texture(1, 1).color(1f, 1f, 1f, 1f);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y, (float)layer).texture(1, 0).color(1f, 1f, 1f, 1f);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }


    public void drawTextureColored(DrawContext context, Identifier texture, int r, int g, int b) {
        drawTextureColored(context, texture, r, g, b, 255);
    }

    public void drawTextureColored(DrawContext context, Identifier texture, int r, int g, int b, int a) {
        var x = getGlobalX();
        var y = getGlobalY();
        var width = getWidth();
        var height = getHeight();
        RenderSystem.setShaderTexture(0, texture);

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float) layer).texture(0, 0).color(r, g, b, a);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y + height, (float)layer).texture(0, 1).color(r, g, b, a);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)layer).texture(1, 1).color(r, g, b, a);
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y, (float)layer).texture(1, 0).color(r, g, b, a);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
    /**
     * Draws a full texture over the entire element.
     */
    public void drawTexture(DrawContext context, Identifier texture, int width, int height) {
        context.drawTexture(texture, getGlobalX(), getGlobalY(), getWidth(), getHeight(),0, 0, width, height, width, height);
    }

    /**
     * Draws a tiled texture over the entire element.
     */
    public void drawTiledTexture(DrawContext context, Identifier texture, int width, int height) {
        context.drawTexture(texture, getGlobalX(), getGlobalY(),0, 0, getWidth(), getHeight(), width, height);
    }
    /**
     * Draws a full texture with the given size position relative to the element's.
     */
    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        context.drawTexture(texture, getGlobalX() + x, getGlobalY() + y, 0,0,width,height);
    }

    private void drawEdges(DrawContext context, Rect2 rect, Identifier texture, int edge_thickness, int texture_width, int texture_height) {
        // top
        context.drawTexture(texture, (int) (rect.getX() + edge_thickness), (int) rect.getY(),rect.width  - (edge_thickness * 2) ,edge_thickness,edge_thickness,0,texture_width - (edge_thickness * 2),edge_thickness,texture_width,texture_height);
        // bottom
        context.drawTexture(texture, (int) (rect.getX() + edge_thickness), (int) (rect.getY() + rect.height - edge_thickness),rect.width  - (edge_thickness * 2) ,edge_thickness,edge_thickness,texture_height - edge_thickness,texture_width - (edge_thickness * 2),edge_thickness,texture_width,texture_height);
        // left
        context.drawTexture(texture, (int) rect.getX(), (int) (rect.getY() + edge_thickness),edge_thickness ,rect.height - (edge_thickness * 2),0, edge_thickness,edge_thickness,texture_height - (edge_thickness * 2),texture_width,texture_height);
        // right
        context.drawTexture(texture, (int) (rect.getX() + rect.width - edge_thickness), (int) (rect.getY() + edge_thickness),edge_thickness ,rect.height - (edge_thickness * 2),texture_width - edge_thickness, edge_thickness,edge_thickness,texture_height - (edge_thickness * 2),texture_width,texture_height);
    }

    private void drawCenter(DrawContext context, Rect2 rect, Identifier texture, int edge_thickness, int texture_width, int texture_height) {
        context.drawTexture(texture, (int) (rect.getX() + edge_thickness), (int) (rect.getY() + edge_thickness),rect.width - (edge_thickness * 2) ,rect.height - (edge_thickness * 2),edge_thickness, edge_thickness,texture_width - (edge_thickness * 2),texture_height - (edge_thickness * 2),texture_width,texture_height);
    }

    private void drawCorners(DrawContext context, Rect2 rect, Identifier texture, int edge_thickness, int texture_width, int texture_height) {
        var max_x = rect.getX() + rect.width;
        var max_y = rect.getY() + rect.height;
        // top left
        context.drawTexture(texture, (int) rect.getX(), (int) rect.getY(),edge_thickness,edge_thickness,0,0,edge_thickness,edge_thickness,texture_width,texture_height);

        // top right
        context.drawTexture(texture, (int) (max_x - edge_thickness), (int) rect.getY(),texture_width - edge_thickness,0,edge_thickness,edge_thickness,texture_width,texture_height);

        // bottom right
        context.drawTexture(texture, (int) (max_x - edge_thickness), (int) (max_y - edge_thickness),edge_thickness,edge_thickness,texture_width - edge_thickness,texture_height - edge_thickness,edge_thickness,edge_thickness,texture_width,texture_height);

        // bottom left
        context.drawTexture(texture, (int) rect.getX(), (int) (max_y - edge_thickness),edge_thickness,edge_thickness,0,texture_height - edge_thickness,edge_thickness,edge_thickness,texture_width,texture_height);
    }
    //endregion
    //region Import Export

    @SuppressWarnings("unchecked")
    public static <E extends ParentableWidgetBase> E fromJson(JsonObject element, String elementName, boolean editMode) {
        E result;
        var properties = element.get("properties").getAsJsonObject();
        var children = element.get("children").getAsJsonObject();
        try {
            var clazz = Class.forName(element.get("class").getAsString());
            result = (E) clazz.getConstructor(int.class, int.class, boolean.class).newInstance(0,0, editMode);
        } catch (Exception e) {
            if (e instanceof InstantiationException) {
                try {
                    var clazz = Class.forName(element.get("class").getAsString());
                    var cannonName = ClassCodeStringBuilder.getSimpleCanonicalName(clazz);
                    var otherDevsClass = "\nJson Load Error:\nElement class '" + cannonName + "' is missing a default init method, please add this to your element class:\n" + String.format("public %s(int x, int y, boolean editMode) {\n}\n", cannonName) + "Class Path: " + clazz.getCanonicalName() + "\n";
                    var oneOfMyClasses = "\nJson Load Error:\nBuilt in element '" + cannonName + "' is missing a default init method, please make a bug report.\n Class Path: " + clazz.getCanonicalName() + "\n";
                    var isMyClass = clazz.getCanonicalName().startsWith("org.shadowmaster435.gooeyeditor");
                    throw new RuntimeException((isMyClass) ? oneOfMyClasses : otherDevsClass);
                } catch (Exception a) {
                    throw new RuntimeException(a);
                }

            } else {
                throw new RuntimeException("\nJson Load Error:\n", e);
            }
        }
        for (Map.Entry<String, JsonElement> propElement : properties.entrySet()) {
            var property = Property.fromJson(propElement.getValue().getAsJsonObject());
            property.getLeft().set(result, property.getRight());
        }
        for (Map.Entry<String, JsonElement> child : children.entrySet()) {
            result.addElement(fromJson(child.getValue().getAsJsonObject(), child.getKey(), editMode));
        }
        result.name = elementName;
        result.imported = true;
        return result;
    }

    public void writeJson(JsonObject object) {
        ArrayList<Property> props = new ArrayList<>();
        props.addAll(Arrays.stream(getProperties()).toList());
        props.addAll(Arrays.stream(getDefaultProperties()).toList());
        JsonObject elem = new JsonObject();
        JsonObject propJson = new JsonObject();
        JsonObject children = new JsonObject();


        for (Property property : props) {
            var prop = new JsonObject();
            property.writeJson(prop, this);
            propJson.add(property.display_name, prop);
        }
        if (this instanceof ParentableWidgetBase e) {
            for (GuiElement element : e.getElements()) {
                element.writeJson(children);
            }
        }
        elem.addProperty("class", getClass().getCanonicalName());
        elem.add("properties", propJson);
        elem.add("children", children);
        object.add(name, elem);
    }


    public void createChildInitString(ClassCodeStringBuilder.MethodStringBuilder builder, Class<?> clazz, String className, GuiElement element, GuiElement par, HashMap<String, Integer> usedNames, String safeParentName) {

        var safeName = GuiEditorScreen.getSafeName(this, usedNames, true);

        element.createLocalInitString(builder, clazz, safeName);
        element.createAssignerSetterString(builder, safeName, usedNames, (par == null));
        builder.line(safeParentName + ".addElement(" + safeName + ");");
    }

    public void createLocalInitString(ClassCodeStringBuilder.MethodStringBuilder builder, Class<?> clazz, String className) {
        builder.assign(ClassCodeStringBuilder.getSimpleCanonicalName(clazz) + " " + className, new ClassCodeStringBuilder.NewInstanceStringBuilder(clazz).add(getX()).add(getY()).add(false));
    }

    public void createAssignerInitInputString(ClassCodeStringBuilder.MethodStringBuilder builder, Class<?> clazz, String className) {
        builder.assign("this." + className, className);
    }


    public void createAssignerSetterString(ClassCodeStringBuilder.MethodStringBuilder methodStringBuilder, String className, HashMap<String, Integer> usedNames, boolean root) {
        ArrayList<Property> props = new ArrayList<>();
        props.addAll(Arrays.stream(getProperties()).toList());
        props.addAll(Arrays.stream(getDefaultProperties()).toList());
        for (Property property : props) {
            if (Objects.equals(property.display_name, "Position")) {
                continue;
            }
            methodStringBuilder.line(getPropertyText(property, null));
        }
        if (root) {
            methodStringBuilder.line("addElement(" + className + ");");
        }

    }

    String getPropertyText(Property property, @Nullable String passedName) {
        var builder = new StringBuilder();
        var prop = property.get(this);
        var name = (passedName != null) ? passedName : this.name;
        assert prop != null;
        var propClassName = prop.getClass().getSimpleName();
        if (property.isMethodSetter(this)) {
            if (prop instanceof Identifier) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append("(Identifier.of(\"").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("\"));\n");
            } else if (propClassName.contains("Vector")) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append("(").append("new ").append(propClassName).append("(");
                String floatCheck = "";
                if (propClassName.contains("f")) {
                    floatCheck = "F";
                }
                try {
                    if (propClassName.contains("2")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck);
                    }
                    if (propClassName.contains("3")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("z").get(prop)).append(floatCheck);
                    }
                    if (propClassName.contains("4")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("z").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("w").get(prop)).append(floatCheck);
                    }
                    builder.append("));");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (propClassName.equals("String")) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append("(\"").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("\");");

            } else if (prop instanceof Float) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append("(").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("F").append(");");

            } else {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append("(").append(Optional.ofNullable(property.get(this)).orElseThrow()).append(");");
            }
        } else {
            if (prop instanceof Identifier) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append(" = ").append("Identifier.of(\"").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("\");");
            } else if (propClassName.contains("Vector")) {
                try {
                    String floatCheck = "";
                    if (propClassName.contains("f")) {
                        floatCheck = "F";
                    }
                    builder.append(name).append(".").append(property.setterMethodOrFieldName).append(" = ").append("new ").append(propClassName).append("(");
                    if (propClassName.contains("2")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck);
                    }
                    if (propClassName.contains("3")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("z").get(prop)).append(floatCheck);
                    }
                    if (propClassName.contains("4")) {
                        builder.append(prop.getClass().getField("x").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("y").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("z").get(prop)).append(floatCheck).append(", ");
                        builder.append(prop.getClass().getField("w").get(prop)).append(floatCheck);
                    }
                    builder.append(");");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (propClassName.equals("String")) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append(" = \"").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("\";");
            } else if (prop instanceof Float) {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append(" = ").append(Optional.ofNullable(property.get(this)).orElseThrow()).append("F").append(";");
            } else {
                builder.append(name).append(".").append(property.setterMethodOrFieldName).append(" = ").append(Optional.ofNullable(property.get(this)).orElseThrow()).append(";");
            }
        }
        return builder.toString();
    }
    //endregion
}
