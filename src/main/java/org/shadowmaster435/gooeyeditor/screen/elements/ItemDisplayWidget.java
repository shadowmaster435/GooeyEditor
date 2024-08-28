package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemDisplayWidget extends ParentableWidgetBase {

    public Identifier item = Identifier.of("minecraft:dirt");
    public int count = 1;

    public ItemDisplayWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public ItemDisplayWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        try {
            drawItem(context, new ItemStack(Registries.ITEM.get(item), count), getX(), getY(), getWidth(), getHeight());
        } catch (Exception ignored) {}
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var count = new Property("Count", "count", "count", Integer.class);
        var item = new Property("Display Item", "item", "item", Identifier.class);
        return new Property[]{item, count};
    }
}
