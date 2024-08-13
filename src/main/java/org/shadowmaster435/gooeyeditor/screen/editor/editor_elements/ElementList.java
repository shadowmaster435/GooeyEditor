package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.GenericContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.PopupContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableListContainer;

import java.util.HashMap;
import java.util.function.Supplier;

public class ElementList extends GenericContainer implements EditorUtil {

    private PopupContainer popup;
    private GenericContainer clip_box;

    private ScrollableListContainer button_list;
    private HashMap<TextButtonWidget, Supplier<? extends GuiElement>> entries = new HashMap<>();
    private TextButtonWidget close_button;
    private ScrollbarWidget scrollbar;
    private NinePatchTexture bg;
    private boolean open = false;
    private GuiEditorScreen screen;

    public ElementList(int x, int y, int w, int h, GuiEditorScreen screen, boolean editMode) {
        super(x, y, w, h, editMode);
        this.screen = screen;
        setPosition(getScreenCenteredPos(this));
        init();
    }

    public  <E extends GuiElement> void registerElement(String display_name, Supplier<E> create_function) {
        var button = new TextButtonWidget(0, 0, Text.of(display_name), false);
        entries.put(button, create_function);
        button.setPressFunction(this::create);
        button_list.addElement(button);
        button_list.arrange();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        push(context);
        context.getMatrices().translate(0,0,515);
        pop(context);
        super.render(context, mouseX, mouseY, delta);

    }

    private <W extends GuiButton> void create(W widget) {
        this.close((TextButtonWidget) widget);

        var entry = entries.get(widget).get();
        entry.layer = screen.getCurrentLayer();
        screen.toAdd.add(entry);
    }

    private <W extends GuiButton> void close(W widget) {
        popup.close();
        this.open = false;
    }

    public void open() {
        popup.open(0, 0);
        this.open = true;
    }

    public boolean isOpen() {
        return this.open;
    }


    private void init() {
        PopupContainer popup = new PopupContainer(getWidth(), getHeight(), false);
        ScrollbarWidget scrollbar = new ScrollbarWidget(getX() + getWidth() - 13, getY() + 5, 8, getHeight() - 11, false);
        ScrollableListContainer button_list = new ScrollableListContainer(getX() + 16, getY() + 16, getWidth() - 12, getHeight() - 24, scrollbar, 4, false);
        button_list.scissor(true);
        TextButtonWidget close_button = new TextButtonWidget(getX() + 8, getY() + 8, Text.of("X"), false);
        NinePatchTexture bg = new NinePatchTexture(getX(), getY(), getWidth(), getHeight(), NinePatchTexture.GUI_BOX, false);
        addElement(popup);
        popup.addElement(scrollbar);
        popup.addElement(bg);
        popup.addElement(button_list);
        popup.addElement(close_button);

        close_button.setPressFunction(this::close);

        this.popup = popup;
        this.scrollbar = scrollbar;
        this.bg = bg;
        this.button_list = button_list;
        this.close_button = close_button;

    }


}
