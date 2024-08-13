package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.ParentableWidgetBase;
import org.shadowmaster435.gooeyeditor.screen.elements.TextField;

public class IdentifierWidget extends ParentableWidgetBase {


    public IdentifierWidget(int x, int y, int w) {
        super(x, y, w, 0, false);
        createFields(x, y, w, 12, 2);
    }

    public void createFields(int x, int y, int w, int field_height, int field_spacing) {
        var id = new TextField(x, y, w, field_height, MinecraftClient.getInstance().textRenderer, false);
        var path = new TextField(x, y + (field_height + field_spacing), w, field_height, MinecraftClient.getInstance().textRenderer, false);
        widgets.add(id);
        widgets.add(path);
        id.setMaxLength(1000);
        path.setMaxLength(1000);
        getRect().height += (field_height + field_spacing) * 2;
    }

    public Identifier getValue() {
        return Identifier.of(((TextField) widgets.get(0)).getText(), ((TextField) widgets.get(1)).getText());
    }

    public void setText(Identifier identifier) {
        if (identifier == null) {
            return;
        }
        ((TextField)(widgets.get(0))).setText(identifier.getNamespace());
        ((TextField)(widgets.get(1))).setText(identifier.getPath());

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
    @Override
    public boolean isFocused() {
        for (GuiElement element : this) {
            if (element.isFocused()) {
                return true;
            }
        }
        return false;
    }

    public Property[] getProperties() {
        return new Property[0];
    }
}
