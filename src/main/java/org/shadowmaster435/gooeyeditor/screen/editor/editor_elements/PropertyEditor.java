package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.*;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;
import org.shadowmaster435.gooeyeditor.screen.elements.records.ScrollbarWidgetData;
import org.shadowmaster435.gooeyeditor.screen.util.Rect2;

import java.util.*;

public class PropertyEditor extends GuiElement {

    private final HashMap<SealedGuiElement.Property, SealedGuiElement> properties = new HashMap<>();
    private ScrollableContainer listContainer;
    private ScrollbarWidget scrollbar;
    public boolean shouldRenderText = false;
    public Rect2 editorRect = new Rect2();
    public boolean hoveringContextMenu = false;
    public GuiEditorScreen screen = null;

    public PropertyEditor(int x, int y, boolean editMode) {
        super(MinecraftClient.getInstance().getWindow().getScaledWidth() - 64, 32, 32, 128, editMode);
        initScrollStuff();
    }

    public void align() {
        setPosition(MinecraftClient.getInstance().getWindow().getScaledWidth() - 64, 32);
        listContainer.setPosition(MinecraftClient.getInstance().getWindow().getScaledWidth() - 110, 12);
        listContainer.setSize(getWidth(), getHeight());
        scrollbar.setPosition(MinecraftClient.getInstance().getWindow().getScaledWidth() - 128, 0);
        scrollbar.setSize(16,  MinecraftClient.getInstance().getWindow().getScaledHeight());
    }

    public void initScrollStuff() {
        var scrollbar = new ScrollbarWidget(MinecraftClient.getInstance().getWindow().getScaledWidth() - 128, 0, 16,  MinecraftClient.getInstance().getWindow().getScaledHeight() , getTextureData(), false);
        var container = new ScrollableContainer(MinecraftClient.getInstance().getWindow().getScaledWidth() - 110, 12, getWidth(), getHeight(), MinecraftClient.getInstance().getWindow().getScaledHeight(), scrollbar, 12, false);
        this.listContainer = container;
        addElement(container);
        this.scrollbar = scrollbar;
        scrollbar.layer = 513;
        addElement(scrollbar);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!hoveringContextMenu) {
            return super.mouseClicked(mouseX, mouseY, button);
        } else {
            return false;
        }
    }

    private ScrollbarWidgetData getTextureData() {
        var tdata = new NinePatchTextureData(16,16,5,Identifier.of(GooeyEditor.id, "textures/gui/slot.png"));
        return new ScrollbarWidgetData(tdata,Identifier.of(GooeyEditor.id, "textures/gui/scroll_grabber.png"),16,16,2, 16);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (listContainer == null) {
            return;
        }


        scrollbar.setVisible(shouldRenderText);
        if (shouldRenderText) {
            listContainer.arrange();
            listContainer.render(context, mouseX,mouseY, delta);
            push(context);
            context.getMatrices().translate(0,0,512);
            editorRect.setRect(context.getScaledWindowWidth() - 128, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            context.fill(context.getScaledWindowWidth() - 128, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), ColorHelper.Argb.getArgb(127, 127, 127));

            for (Property property : properties.keySet()) {
                context.drawText(MinecraftClient.getInstance().textRenderer,property.display_name(), properties.get(property).getGlobalX(), properties.get(property).getGlobalY() - 8, ColorHelper.Argb.getArgb(255,255,255), true);

            }
            pop(context);
        }
        organizePropertyElements();
    }

    public <E extends SealedGuiElement> void loadProperties(E element, boolean genTree) {
        properties.clear();
        var props = new ArrayList<>(Arrays.stream(element.getDefaultProperties()).toList());
        props.addAll(Arrays.asList(element.getProperties()));
        listContainer.clearChildren();
        if (element instanceof GuiElement base && genTree) {
            screen.tree.createTreeForElement(base);
        }
        for (SealedGuiElement.Property prop : props) {
            if (Objects.equals(prop.display_name(), "Localize Position") && !element.showsParentOffsetButton) {
                continue;
            }
            var elem = widgetForType(prop.aClass());
            properties.put(prop, elem);
            listContainer.addElement(elem);
            elem.layer = 513;
        }
    }

    public void renderText(boolean val) {
        shouldRenderText = val;

    }

    public void unloadProperties() {
        listContainer.clearChildren();
        screen.tree.clearChildren();
        shouldRenderText = false;
    }


    public void updateInputText(SealedGuiElement source_element) {
        for (Property property : properties.keySet()) {
            var element = properties.get(property);
            var clazz = property.aClass();
            if (clazz == Vector2d.class || clazz == Vector2f.class || clazz == Vector2i.class || clazz == Vector3d.class || clazz == Vector3f.class || clazz == Vector3i.class || clazz == Vector4d.class || clazz == Vector4f.class || clazz == Vector4i.class) {
                if (property.get(source_element) == null) {
                    System.out.println(property.display_name());
                }
                ((VectorWidget) (element)).setText(property.get(source_element));
            } else if (clazz == Identifier.class) {
                ((IdentifierWidget) (element)).setText(property.get(source_element));
            } else if (clazz == String.class) {
                ((TextField) (element)).setText(property.get(source_element));
            } else if (clazz == Boolean.class) {
                ((TextureButtonWidget) (element)).pressed = (Boolean.TRUE.equals(property.get(source_element)));

            } else {
                ((NumberFieldWidget) (element)).setValue(property.get(source_element));
            }
        }
    }
    public void updateProperties(SealedGuiElement source_element) {
        for (Property property : properties.keySet()) {
            try {
                var element = properties.get(property);
                var clazz = property.aClass();
                if (clazz == Integer.class) {
                    property.set(source_element, ((NumberFieldWidget) element).getInt());
                    continue;
                }
                if (clazz == Boolean.class) {
                    property.set(source_element, ((TextureButtonWidget) element).pressed);
                    continue;
                }
                if (clazz == Float.class) {
                    property.set(source_element, ((NumberFieldWidget) element).getFloat());
                    continue;
                }
                if (clazz == Double.class) {
                    property.set(source_element, ((NumberFieldWidget) element).getDouble());
                    continue;
                }
                if (clazz == Identifier.class) {
                    property.set(source_element, ((IdentifierWidget) element).getValue());
                    continue;
                }
                if (clazz == Vector2d.class || clazz == Vector2f.class || clazz == Vector2i.class || clazz == Vector3d.class || clazz == Vector3f.class || clazz == Vector3i.class || clazz == Vector4d.class || clazz == Vector4f.class || clazz == Vector4i.class) {
                    property.set(source_element, clazz.cast(((VectorWidget) element).getValue()));
                    continue;
                }
                if (clazz == String.class) {
                    property.set(source_element, ((TextField) element).getText());
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollbar.mouseScrolled(mouseX,mouseY,horizontalAmount,verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }

    public boolean isPropertyFocused() {
        for (SealedGuiElement element : listContainer) {
            if (element.isFocused()) {
                return true;
            }
        }
        return false;
    }

    public boolean hoveringProperty(int mouseX, int mouseY) {
        for (SealedGuiElement element : listContainer) {
            if (element.isMouseOver(mouseX, mouseY) || scrollbar.isMouseOver(mouseX,mouseY)) {
                return true;
            }
        }
        return false;
    }

    public void organizePropertyElements() {
        var current_y = 0;
        for (SealedGuiElement prop : listContainer) {
            if (!(prop instanceof TextureButtonWidget)) {
                prop.setWidth(100);
            }
            prop.setY(current_y);
            current_y += prop.getHeight() + 12;

        }
    }

    public SealedGuiElement widgetForType(Class<?> clazz) {
        if (clazz == Float.class || clazz == Double.class || clazz == Integer.class || clazz == Long.class) {
            return new NumberFieldWidget(0,0, 32, 12, MinecraftClient.getInstance().textRenderer, false);
        }
        if (clazz == Boolean.class) {
            var widget = new TextureButtonWidget(0,0,20, 20, TextureButtonWidget.CHECKBOX, false);
            widget.toggle_mode = true;
            return widget;
        }
        if (clazz == Identifier.class) {
            return new IdentifierWidget(0,0,64);
        }
        if (
                clazz == Vector2d.class || clazz == Vector2f.class || clazz == Vector2i.class ||
                clazz == Vector3d.class || clazz == Vector3f.class || clazz == Vector3i.class ||
                clazz == Vector4d.class || clazz == Vector4f.class || clazz == Vector4i.class
        ) {
            return new VectorWidget(clazz, 0, 0, 64, false);
        }
        if (clazz == String.class) {
            return new TextField(0,0,64,12, MinecraftClient.getInstance().textRenderer, false);
        }

        return null;
    }


}
