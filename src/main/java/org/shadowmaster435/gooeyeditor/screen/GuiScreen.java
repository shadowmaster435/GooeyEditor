package org.shadowmaster435.gooeyeditor.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.PlayerInventoryWidget;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotGridWidget;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiScreen extends Screen {

    private final HashMap<String, SealedGuiElement> elements = new HashMap<>();
    private boolean initialized = false;

    public GuiScreen() {
        super(Text.of(""));
        init();
    }

    @Override
    public void tick() {
        super.tick();
        if (client != null && !initialized) {
            refreshElements();
            initElements();
            clinit();
            initialized = true;
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    public abstract void clinit();

    public abstract void initElements();

    public SealedGuiElement[] getElements() {
        return elements.values().toArray(new SealedGuiElement[]{});
    }

    public SealedGuiElement getElement(String key) {
        return elements.getOrDefault(key, null);
    }

    public final void refreshElements() {
        elements.clear();
        for (Element element : children()) {
            if (element instanceof SealedGuiElement guiElement) {
                elements.put(guiElement.name, guiElement);
            }
        }
    }

    public final ArrayList<SealedGuiElement> getEditableElements() {
        refreshElements();
        ArrayList<SealedGuiElement> result = new ArrayList<>();
        for (String key : elements.keySet()) {
            var guiElement = elements.get(key);
            guiElement.setEditMode(true);
            result.add(guiElement);
        }
        return result;
    }

    public SealedGuiElement getHoveredElement(double mouseX, double mouseY) {
        for (SealedGuiElement element : elements.values()) {
            if (element.isMouseOver(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }

    public SlotWidget getHoveredSlot(double mouseX, double mouseY) {
        for (SealedGuiElement element : elements.values()) {
            if (element.isMouseOver(mouseX, mouseY) && element instanceof SlotGridWidget slotWidget) {
                var child = slotWidget.getHoveredChild((int) mouseX, (int) mouseY);
                if (child instanceof SlotWidget e) {

                    return e;
                }
            }
            if (element instanceof PlayerInventoryWidget playerInventoryWidget) {
                return playerInventoryWidget.getHoveredSlot((int) mouseX, (int) mouseY);
            }
            if (element.isMouseOver(mouseX, mouseY) && element instanceof SlotWidget slotWidget) {
                return slotWidget;
            }
        }
        return null;
    }

    /**
     * @return All elements as json.
     */
    public final JsonObject toJson() {
        var result = new JsonObject();
        for (String key : elements.keySet()) {
            var element = elements.get(key);
            element.writeJson(result);

        }
        return result;
    }

    public void addElement(SealedGuiElement element) {
        addDrawableChild(element);
    }

    @Override
    public <E extends Element & Drawable & Selectable> E addDrawableChild(E drawableElement) {
        if (drawableElement instanceof SealedGuiElement e) {
            elements.put(e.name, e);
        }
        return super.addDrawableChild(drawableElement);
    }

    /**
     * Loads all elements from json
     */
    public final void fromJson(JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            var element = SealedGuiElement.fromJson(entry.getValue().getAsJsonObject(), entry.getKey(), false);
            addDrawableChild(element);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public abstract boolean isClickOutsideBounds(double mouseX, double mouseY, int button);

}
