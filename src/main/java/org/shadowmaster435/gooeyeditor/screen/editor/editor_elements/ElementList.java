package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.GenericContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.PaginatedListContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.PopupContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ScrollableListContainer;

import java.util.HashMap;
import java.util.function.Supplier;

public class ElementList extends GenericContainer implements EditorUtil {

    private PopupContainer popup;
    private GenericContainer clip_box;

    private PaginatedListContainer button_list;
    private final HashMap<TextButtonWidget, Supplier<? extends GuiElement>> entries = new HashMap<>();
    private TextButtonWidget close_button;
    private ScrollbarWidget scrollbar;
    private NinePatchTexture bg;
    private boolean open = false;
    private final GuiEditorScreen screen;
    public GuiElement childToAddTo = null;
    public TextureButtonWidget left;
    public TextureButtonWidget right;

    public ElementList(int x, int y, int w, int h, GuiEditorScreen screen, boolean editMode) {
        super(x, y, w, h, editMode);
        this.screen = screen;
        setPosition(getScreenCenteredPos(this));
        initElements();
    }

    public  <E extends GuiElement> void registerElement(String display_name, Supplier<E> create_function) {
        var button = new TextButtonWidget(0, 0, display_name, false);
        button.setHeight(8);
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
        scissor(false);
        super.render(context, mouseX, mouseY, delta);
      //  System.out.println(button_list.getElements().size());

    }

    private <W extends GuiButton> void create(W widget) {
        this.close((TextButtonWidget) widget);
        var entry = entries.get(widget).get();
        entry.layer = screen.getCurrentLayer();
        if (childToAddTo != null && screen.hasSelectedElement() && childToAddTo instanceof ParentableWidgetBase a) {

            screen.toAddToChild.put(entry, a);
        } else {

            screen.toAdd.add(entry);
        }
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
        ScrollbarWidget scrollbar = new ScrollbarWidget(getWidth() - 13, 5, 8, getHeight() - 11, false);
        ScrollableListContainer button_list = new ScrollableListContainer(16, 16, getWidth() - 12, getHeight() - 20, scrollbar, 4, false);
        button_list.reversed = true;
        scrollbar.scroll_delta = 4;
        button_list.scissor(true);
        TextButtonWidget close_button = new TextButtonWidget(8, 8, "X", false);
        NinePatchTexture bg = new NinePatchTexture(0, 0, getWidth(), getHeight(), NinePatchTexture.BOX_PANEL, false);
        addElement(popup);
        popup.addElement(scrollbar);
        popup.addElement(bg);
        popup.addElement(button_list);
        popup.addElement(close_button);
        close_button.setPressFunction(this::close);

        this.popup = popup;
        this.scrollbar = scrollbar;
        this.bg = bg;
    //    this.button_list = button_list;
        this.close_button = close_button;

    }
    private void initElements() {
        PopupContainer popup = new PopupContainer(getWidth(), getHeight(), false);
        addElement(popup);
        this.popup = popup;

        NinePatchTexture ninePatch = new NinePatchTexture(87, 63, false);
        ninePatch.texture_width = 16;
        ninePatch.texture_height = 16;
        ninePatch.edge_thickness = 6;
        ninePatch.texture = Identifier.of("gooeyeditor:textures/gui/gui_box.png");
        ninePatch.name = "ninePatch";
        ninePatch.setSize(new Vector2i(253, 115));
        ninePatch.setScale(new Vector2f(1.0F, 1.0F));
        ninePatch.rotation = 0.0F;
        ninePatch.layer = 512;
        ninePatch.setOrigin(new Vector2i(0, 0));
        ninePatch.center_origin = false;
        ninePatch.center_screen = true;
        popup.addElement(ninePatch);
        PaginatedListContainer buttonList = new PaginatedListContainer(21, 5, false);
        buttonList.element_spacing = 8;
        buttonList.vertical = true;
        buttonList.setCurrentPage(0);
        buttonList.name = "buttonList";
        buttonList.setSize(new Vector2i(200, 90));
        buttonList.setScale(new Vector2f(1.0F, 1.0F));
        buttonList.rotation = 0.0F;
        buttonList.layer = 513;
        buttonList.setOrigin(new Vector2i(0, 0));
        buttonList.center_origin = false;
        buttonList.center_screen = true;
        ninePatch.addElement(buttonList);
        this.button_list = buttonList;
        this.bg = ninePatch;
        TextButtonWidget closeButton = new TextButtonWidget(8, 8, false);
        closeButton.text = "X";
        closeButton.name = "closeButton";
        closeButton.setSize(new Vector2i(22, 40));
        closeButton.setScale(new Vector2f(1.0F, 1.0F));
        closeButton.rotation = 0.0F;
        closeButton.layer = 513;
        closeButton.setOrigin(new Vector2i(0, 0));
        closeButton.center_origin = false;
        closeButton.center_screen = false;
        ninePatch.addElement(closeButton);
        this.close_button = closeButton;
        TextureButtonWidget left = new TextureButtonWidget(7, 96, false);
        left.setData(TextureButtonWidget.ARROW_LEFT);
        left.name = "left";
        left.setSize(new Vector2i(6, 11));
        left.setScale(new Vector2f(1.0F, 1.0F));
        left.rotation = 0.0F;
        left.layer = 513;
        left.setOrigin(new Vector2i(0, 0));
        left.center_origin = false;
        left.center_screen = false;
        ninePatch.addElement(left);
        this.left = left;
        TextureButtonWidget right = new TextureButtonWidget(15, 96, false);
        right.setData(TextureButtonWidget.ARROW_RIGHT);
        right.name = "right";
        right.setSize(new Vector2i(6, 11));
        right.setScale(new Vector2f(1.0F, 1.0F));
        right.rotation = 0.0F;
        right.layer = 513;
        right.setOrigin(new Vector2i(0, 0));
        right.center_origin = false;
        right.center_screen = false;
        ninePatch.addElement(right);
        this.right = right;
        buttonList.bindButtonToNext(right);
        buttonList.bindButtonToPrevious(left);
        closeButton.setPressFunction(this::close);
    }


}
