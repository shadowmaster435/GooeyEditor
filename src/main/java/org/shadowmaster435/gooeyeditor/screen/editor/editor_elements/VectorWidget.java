package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.joml.*;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.NumberFieldWidget;
import org.shadowmaster435.gooeyeditor.screen.elements.ParentableWidgetBase;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;

import java.util.ArrayList;

public class VectorWidget extends ParentableWidgetBase {

    private NumberType type;
    private final ArrayList<Number> elements = new ArrayList<>();
    private int element_count;
    private boolean isRect;

    public VectorWidget(NumberType type, int x, int y, int w, int element_count, boolean isRect, Number... preset) {
        super(x, y, w, 0, false);
        init(type, x, y, w, element_count, isRect, preset);
    }

    public VectorWidget(Class<?> clazz, int x, int y, int w, boolean isRect, Number... preset) {
        super(x, y, w, 0, false);

        int element_count = 0;
        NumberType nType = null;
        if (clazz.getSimpleName().endsWith("d")) {
            nType = NumberType.D;
        }
        if (clazz.getSimpleName().endsWith("f")) {
            nType = NumberType.F;
        }
        if (clazz.getSimpleName().endsWith("i")) {
            nType = NumberType.I;
        }
        if (clazz.getSimpleName().contains("2")) {
            element_count = 2;
        }
        if (clazz.getSimpleName().contains("3")) {
            element_count = 3;
        }
        if (clazz.getSimpleName().contains("4")) {
            element_count = 4;
        }
        init(nType, x, y, w, element_count, isRect, preset);
    }

    private void init(NumberType type, int x, int y, int w, int element_count, boolean isRect, Number... preset) {

        if (isRect) {
            this.type = NumberType.D;
            this.element_count = 4;
            elements.add(preset[0]);
            elements.add(preset[1]);
            elements.add(preset[2]);
            elements.add(preset[3]);

        } else {
            this.type = type;
            for (int i = 0; i < element_count; ++i) {
                if (i < preset.length) {
                    elements.add(i, preset[i]);

                } else {
                    elements.add(i, 0);
                }
            }
            this.element_count = element_count;

        }
        this.isRect = isRect;
        createFields(x, y, w, 12, 2);
    }

    private void updateNumbers(int index) {
        switch (type) {
            case I -> elements.set(index, ((NumberFieldWidget) widgets.get(index)).getInt());
            case D -> elements.set(index, ((NumberFieldWidget) widgets.get(index)).getDouble());
            case F -> elements.set(index, ((NumberFieldWidget) widgets.get(index)).getFloat());
        }
    }

    public <V> Number[] convertVector(V vec) {
        var clazz = vec.getClass();
        ArrayList<Number> list = new ArrayList<>();
        if (clazz.getSimpleName().contains("2")) {
            try {
                list.add(0, (Number) vec.getClass().getField("x").get(vec));
                list.add(1, (Number) vec.getClass().getField("y").get(vec));
            } catch (Exception ignored) {}
        }
        if (clazz.getSimpleName().contains("3")) {
            try {
                list.add(0, (Number) vec.getClass().getField("x").get(vec));
                list.add(1, (Number) vec.getClass().getField("y").get(vec));
                list.add(2, (Number) vec.getClass().getField("z").get(vec));

            } catch (Exception ignored) {}
        }
        if (clazz.getSimpleName().contains("4")) {
            try {
                list.add(0, (Number) vec.getClass().getField("x").get(vec));
                list.add(1, (Number) vec.getClass().getField("y").get(vec));
                list.add(2, (Number) vec.getClass().getField("z").get(vec));
                list.add(3, (Number) vec.getClass().getField("w").get(vec));
            } catch (Exception ignored) {}
        }
        return list.toArray(new Number[]{});
    }

    public <V> void setText(V vec) {
        var elements = convertVector(vec);
        ((NumberFieldWidget) (widgets.get(0))).setText(String.valueOf(elements[0]));
        ((NumberFieldWidget) (widgets.get(1))).setText(String.valueOf(elements[1]));
        if (element_count > 2) {
            ((NumberFieldWidget) (widgets.get(2))).setText(String.valueOf(elements[2]));
        }
        if (element_count > 3) {
            ((NumberFieldWidget) (widgets.get(3))).setText(String.valueOf(elements[3]));
        }
    }

    private void createFields(int x, int y, int w, int field_height, int field_spacing) {
        var x_field = new NumberFieldWidget(x, y, w, field_height, MinecraftClient.getInstance().textRenderer, false);
        var y_field = new NumberFieldWidget(x, y, w, field_height, MinecraftClient.getInstance().textRenderer, false);
        x_field.setText(String.valueOf(elements.get(0)));
        y_field.setText(String.valueOf(elements.get(1)));
        x_field.setPlaceholder(Text.of("x"));
        y_field.setPlaceholder(Text.of("y"));
        x_field.setChangedListener((a) -> updateNumbers(0));
        y_field.setChangedListener((a) -> updateNumbers(1));
        widgets.add(x_field);
        widgets.add(y_field);
        getRect().height += (field_height + 2) * 2;
        if (element_count > 2) {
            var z_field = new NumberFieldWidget(x, y + ((field_height + field_spacing) * 2), w, field_height, MinecraftClient.getInstance().textRenderer, false);
            if (isRect) {
                z_field.setPlaceholder(Text.of("width"));
            } else {
                z_field.setPlaceholder(Text.of("z"));
            }
            z_field.setText(String.valueOf(elements.get(2)));

            z_field.setChangedListener((a) -> updateNumbers(2));
            getRect().height += (field_height + field_spacing);
            widgets.add(z_field);
        }
        if (element_count > 3) {
            var w_field = new NumberFieldWidget(x, y + ((field_height + field_spacing) * 3), w, field_height, MinecraftClient.getInstance().textRenderer, false);
            if (isRect) {
                w_field.setPlaceholder(Text.of("height"));
            } else {
                w_field.setPlaceholder(Text.of("w"));
            }
            w_field.setText(String.valueOf(elements.get(3)));

            w_field.setChangedListener((a) -> updateNumbers(3));
            getRect().height += (field_height + field_spacing);
            widgets.add(w_field);
        }
        getRect().height -= field_spacing;
    }

    public <T> T getValue() {
        return (T) type.getValueOf(this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        var current_y = 0;
        for (GuiElement element : this) {
            element.setWidth(getWidth());
            element.setX(getX());
            element.setY(current_y + getY());
            current_y += element.getHeight() + 2;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    public enum NumberType {
        I(Vector2i.class, Vector3i.class, Vector4i.class),
        F(Vector2f.class, Vector3f.class, Vector4f.class),
        D(Vector2d.class, Vector3d.class, Vector4d.class);


        final Class<?> vec2_class;
        final Class<?> vec3_class;
        final Class<?> vec4_class;

        NumberType(Class<?> vec2_class, Class<?> vec3_class, Class<?> vec4_class) {
            this.vec2_class = vec2_class;
            this.vec3_class = vec3_class;
            this.vec4_class = vec4_class;
        }

        public Object getValueOf(VectorWidget widget) {
            var e = widget.elements;
            if (widget.isRect) {
                return new Rect2(e.get(0).intValue(),e.get(1).intValue(),e.get(2).intValue(),e.get(3).intValue());
            } else {
                switch (widget.element_count) {
                    case 2 -> {
                        switch (widget.type) {
                            case I -> {return new Vector2i(e.get(0).intValue(), e.get(1).intValue());}
                            case D -> {return new Vector2d(e.get(0).doubleValue(), e.get(1).doubleValue());}
                            case F -> {return new Vector2f(e.get(0).floatValue(), e.get(1).floatValue());}
                        }
                    }
                    case 3 -> {
                        switch (widget.type) {
                            case I -> {return new Vector3i(e.get(0).intValue(), e.get(1).intValue(), e.get(2).intValue());}
                            case D -> {return new Vector3d(e.get(0).doubleValue(), e.get(1).doubleValue(), e.get(2).doubleValue());}
                            case F -> {return new Vector3f(e.get(0).floatValue(), e.get(1).floatValue(), e.get(2).floatValue());}
                        }
                    }
                    case 4 -> {
                        switch (widget.type) {
                            case I -> {return new Vector4i(e.get(0).intValue(), e.get(1).intValue(), e.get(2).intValue(), e.get(3).intValue());}
                            case D -> {return new Vector4d(e.get(0).doubleValue(), e.get(1).doubleValue(), e.get(2).doubleValue(), e.get(3).intValue());}
                            case F -> {return new Vector4f(e.get(0).floatValue(), e.get(1).floatValue(), e.get(2).floatValue(), e.get(3).intValue());}
                        }
                    }

                }
            }
            return null;
        }
    }

    @Override
    public boolean isFocused() {
        for (GuiElement element : this) {
            if (element.isFocused()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}