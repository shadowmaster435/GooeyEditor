package org.shadowmaster435.gooeyeditor.screen.editor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.*;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.BaseContainer;
import org.shadowmaster435.gooeyeditor.util.InputHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.shadowmaster435.gooeyeditor.GooeyEditor.warn;

public class GuiEditorScreen extends Screen implements EditorUtil {

    public static final int EDITOR_LAYERS_START = 512;
    public final ArrayList<GuiElement> toAdd = new ArrayList<>();
    public final HashMap<GuiElement, ParentableWidgetBase> toAddToChild = new HashMap<>();

    private final Screen parent;
    private final String fieldText = "\tpublic %s;\n";
    private final String fieldAssignerText = "\t\tvar %1$s = new %2$s(%3$s);\n\t\t%4$s\n\t\tthis.%1$s = %1$s;\n\n";
    private final String initText = "\t@Override\n\tpublic void initElements() {\n%s\t}\n";
    private final String getText = "\n\t@Override\n\tpublic GuiElement[] getElements() {\n\t\treturn new GuiElement[]{%s};\n\t}\n";
    private final String elementImport = "import org.shadowmaster435.gooeyeditor.screen.elements.*;\n";
    private final String jomlImport = "import org.joml.*;\n";
    private final String identifierImport = "import " + Identifier.class.getCanonicalName() + ";\n";
    private final String guiScreenImport = "import org.shadowmaster435.gooeyeditor.screen.GuiScreen;\n";
    private final String classString = "\npublic class %1$s extends GuiScreen {\n\n\t//This default init function is required if you want to load this screen in the editor\n\t//will throw an exception if excluded\n\tpublic %1$s() {\n\t\tsuper();\n\t}\n";
    private final HashMap<String, Integer> usedNames = new HashMap<>();
    public ButtonWidget button1;
    public GuiElement element;
    public ButtonWidget dragged_button;
    public TextureButtonWidget layer_editing;
    public TextureButtonWidget showContainers;
    public WidgetTree tree;
    private DropDownWidget file;
    private TextButtonWidget save;
    private DropDownWidget create;
    private TextButtonWidget color_rect;
    private GuiElement selected_element = null;
    private IdentifierWidget w1;
    private PropertyEditor propertyEditor;
    private TextButtonWidget add;
    private ElementList elementList;
    private SpinboxWidget displayed_layer;
    private ContextPopupWidget contextMenu;
    private TextButtonWidget contextEditButton;
    private TextButtonWidget contextDeleteButton;
    private TextButtonWidget contextAddButton;
    private TextButtonWidget contextChildButton;

    public GuiEditorScreen(Screen screen) {
        super(Text.empty());
        this.parent = screen;
//        if (GooeyEditorClient.hsv == null) {
//            var arr = new HSVTexture[360];
//            for (int h = 0; h < 360; ++h) {
//                arr[h] = new HSVTexture(h / 360f);
//            }
//            GooeyEditorClient.hsv = arr;
//        }
    }

    public static <W extends GuiButton> void tstfunc(W widget) {
         System.out.println("yep");
    }

    public void loadScreen(String name) {
        try {
            var clazz = GooeyEditor.getClassForDisplayName(name).getConstructor();
            var obj = clazz.newInstance();
            toAdd.addAll(obj.getEditableElements());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public int getCurrentLayer() {
        return displayed_layer.getInt();
    }

    public boolean isLayerMode() {
        return layer_editing.pressed;
    }

    public void updateProperties() {
        if (selected_element != null) {

            if (propertyEditor.isPropertyFocused()) {
                propertyEditor.updateProperties(selected_element);
            } else {

                propertyEditor.updateInputText(selected_element);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var hoveringPropertyEditor = hoveringEditorElement((int) mouseX, (int) mouseY);
        if (elementList.isOpen()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (contextMenu.isOpen()) {
            contextMenu.tryClose((int) mouseX, (int) mouseY);
        } else {
            if (button == 1) {
                contextMenu.removeElement(contextChildButton);
                contextMenu.removeElement(contextDeleteButton);
                contextMenu.removeElement(contextEditButton);

                contextMenu.list.arrange();
                contextMenu.open((int) mouseX, (int) mouseY);
            }
        }
        if (selected_element == null) {
            for (Element element1 : children()) {
                if (element1 instanceof GuiElement element2) {
                    if (element2 instanceof ParentableWidgetBase widgetBase) {
                        AtomicBoolean stop = new AtomicBoolean(false);
                        widgetBase.forEachInBranch((element3, parent, d) -> {
                            if (!stop.get()) {
                                if (element3.isMouseOver(mouseX, mouseY) && element3.isEditMode() && isElementLayerVisible(element3)) {
                                    selectElement(element3);
                                    stop.set(true);
                                }
                            }
                        }, 0);
                        if (stop.get()) {
                            return super.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                    if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && isElementLayerVisible(element2)) {
                        selectElement(element2);

                        return super.mouseClicked(mouseX, mouseY, button);
                    }

                }
            }
        } else {
            if (!hoveringPropertyEditor && selected_element.isMouseOver(mouseX, mouseY) && selected_element.selected && selected_element.isEditMode() && isElementLayerVisible(selected_element)) {

                if (button == 0) {
                    selected_element.startTransform((int) mouseX, (int) mouseY);
                }
                if (button == 1 && selected_element.isMouseOver(mouseX, mouseY)) {

                    contextMenu.removeElement(contextChildButton);
                    contextMenu.removeElement(contextAddButton);
                    contextMenu.addElement(contextEditButton);
                    contextMenu.addElement(contextAddButton);
                    contextMenu.addElement(contextChildButton);

                    contextMenu.addElement(contextDeleteButton);
                    contextMenu.list.arrange();

                    contextMenu.open((int) mouseX, (int) mouseY);
                }
              //  propertyEditor.renderText(true); propertyEditor.loadProperties(selected_element);

            } else {

                for (Element element1 : children()) {
                    if (element1 instanceof GuiElement element2) {
                        if (element2 instanceof ParentableWidgetBase widgetBase) {
                            AtomicBoolean stop = new AtomicBoolean(false);
                            widgetBase.forEachInBranch((element3, parent, d) -> {
                                if (!stop.get()) {
                                    if (element3.isMouseOver(mouseX, mouseY) && element3.isEditMode() && isElementLayerVisible(element3)) {
                                        selectElement(element3);
                                        stop.set(true);
                                    }
                                }
                            }, 0);
                            if (stop.get()) {
                                return super.mouseClicked(mouseX, mouseY, button);
                            }
                        }
                        if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && isElementLayerVisible(element2)) {
                            if (element2 == selected_element) {
                                return super.mouseClicked(mouseX, mouseY, button);
                            }
                            selectElement(element2);

                            return super.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                }
                if (!hoveringEditorElement((int) mouseX, (int) mouseY)) {
                    propertyEditor.unloadProperties();

                    selected_element.selected = false;
                    selected_element = null;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Sets selection to provided element and marks previously selected element (if any) as unselected.
     * @param element Element you want selected.
     */
    public void selectElement(GuiElement element) {
        if (selected_element != null) { // unselect previous if not null
            selected_element.selected = false;
        }
        selected_element = element;
        selected_element.selected = true;
        propertyEditor.loadProperties(selected_element);
    }

    private boolean isElementLayerVisible(GuiElement element) {
        return !layer_editing.pressed || element.layer == displayed_layer.getInt();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Element element1 : children()) {
            element1.keyPressed(keyCode, scanCode, modifiers);
        }
        if (selected_element != null && (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) && !propertyEditor.isPropertyFocused()) {
            deleteSelectedElement(null);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void deleteSelectedElement(GuiButton button) {
        remove(selected_element);
        if (!children().contains(selected_element) && selected_element.parent instanceof ParentableWidgetBase p) {
            p.removeElement(selected_element);
        }
        selected_element = null;
        propertyEditor.unloadProperties();
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Element element1 : children()) {
            element1.charTyped(chr, modifiers);
        }
        if (InputHelper.isMiddleMouseHeld) {
           loadScreen("Test");
           // System.out.println(getExportString("Test"));
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (Element element1 : children()) {
            element1.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Element element1 : children()) {
            element1.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        if (dragged_button != null) {
//            dragged_button.setPosition(mouseX, mouseY);
//
//        }
//        element.rotation += 1f;

        propertyEditor.hoveringContextMenu = contextMenu.isMouseOver(mouseX, mouseY) && contextMenu.isOpen();
        propertyEditor.screen = this;
        var element_found = false;
        if (selected_element != null) {
            if (!hoveringEditorElement(mouseX, mouseY)) {
                selected_element.setCursorType(mouseX, mouseY);
            } else {
                GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
            }
        } else {
            GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
        }


        toAdd.forEach(this::addDrawableChild);
        toAddToChild.forEach((elem, par) -> {
            if (par instanceof ParentableWidgetBase p) {
                p.addElement(elem);
            }
        });
        toAddToChild.clear();
        toAdd.clear();
        super.render(context, mouseX, mouseY, delta);
        updateProperties();
    }

    public void renderPropertyBar(DrawContext context) {
        if (selected_element != null) {
            context.fill(context.getScaledWindowWidth() - 64, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), ColorHelper.Argb.getArgb(127,127,127));
        }
    }

    public boolean hoveringEditorElement(int mouseX, int mouseY) {
        var pEdit = propertyEditor.editorRect.contains(mouseX, mouseY) && propertyEditor.shouldRenderText;
        var ctx = contextMenu.isMouseOver(mouseX, mouseY) && contextMenu.isOpen();
        var tre = tree.isMouseOver(mouseX, mouseY) && isPropertyEditorOpen();
        return pEdit || ctx || tre;
    }

    public boolean isPropertyEditorOpen() {
        return propertyEditor.shouldRenderText;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
      //  System.out.println((Vector3d) w1.getValue());
        super.renderBackground(context, mouseX, mouseY, delta);
        context.fill(0,0, 152, 16, 512, ColorHelper.Argb.getArgb(127,127,127));
        if (file != null) {
            context.getMatrices().push();
            context.getMatrices().translate(0,0,512);

            file.render(context, mouseX, mouseY, delta);
            add.render(context,mouseX,mouseY,delta);

            elementList.render(context,mouseX,mouseY,delta);
            context.getMatrices().pop();
        }
        if (showContainers.pressed) {
            for (Element e : children()) {

                if (e instanceof BaseContainer c) {
                    if (!c.isEditMode()) {
                        continue;
                    }
                    context.fill(c.getX(), c.getY(), c.getX() + c.getWidth(), c.getY() + c.getHeight(), ColorHelper.Argb.getArgb(50,255,255,255));
                } else if (e instanceof ParentableWidgetBase w) {
                    w.forEachInBranch((a, par, d) -> {
                        if (a instanceof BaseContainer c && c.isEditMode()) {
                            context.fill(c.getX(), c.getY(), c.getX() + c.getWidth(), c.getY() + c.getHeight(), ColorHelper.Argb.getArgb(50, 255, 255, 255));
                        }
                    }, 0);
                }
            }
        }
    }

    @Override
    protected void init() {

        var editor = new PropertyEditor(0,32,false);
        addDrawableChild(editor);
        this.propertyEditor = editor;
        initTopBar();
        initContextMenu();
    }

    private void initTopBar() {
        initFileTab();
        initAddTab();
        initLayerTools();
        initContainerShower();
        initWidgetTree();
    }

    private void initContainerShower() {
        var checkbox = new TextureButtonWidget(136, 2, 12, 12, TextureButtonWidget.CHECKBOX, false);
        checkbox.layer = 513;
        checkbox.setTooltip("Show Containers");
        checkbox.positionToolTip(true);
        checkbox.setTooltipPos(128, 32);
        checkbox.toggle_mode = true;
        checkbox.pressed = false;
        addDrawableChild(checkbox);
        this.showContainers = checkbox;
    }

    private void initLayerTools() {
        var spinbox = new SpinboxWidget(64, 2, 52, 12, MinecraftClient.getInstance().textRenderer, false);
        spinbox.layer = 513;
        spinbox.max_value = 500;
        spinbox.min_value = 1;
        spinbox.setTooltip("Enabled Layer");
        spinbox.setTooltipPos(56, 32);
        spinbox.positionToolTip(true);
        spinbox.setValue(1);
        this.displayed_layer = spinbox;
        addDrawableChild(spinbox);
        var checkbox = new TextureButtonWidget(124, 2, 12, 12, TextureButtonWidget.CHECKBOX, false);
        checkbox.layer = 513;
        checkbox.setTooltip("Layer Editing");
        checkbox.positionToolTip(true);
        checkbox.setTooltipPos(116, 32);
        checkbox.toggle_mode = true;
        checkbox.pressed = false;
        addDrawableChild(checkbox);
        this.layer_editing = checkbox;
    }

    private void initAddTab() {
        var add = new TextButtonWidget(32,4,Text.of("Add"), false);
        var elementList = new ElementList(0,0,256, 192, this, false);
        add.setPressFunction(this::openElementList);
        addDrawableChild(add);
        this.add = add;
        this.elementList = elementList;
        addDrawableChild(elementList);
        addDefaultElements();
    }

    private void initContextMenu() {

        var contextMenu = new ContextPopupWidget(128, 96, false);
        contextMenu.layer = 600;
        this.contextMenu = contextMenu;
        addDrawableChild(contextMenu);
        var edit = new TextButtonWidget(0,0,Text.of("Edit"), false);
        edit.setPressFunction((a) -> {propertyEditor.renderText(true); contextMenu.close(); if (selected_element instanceof ParentableWidgetBase widgetBase) tree.createTreeForElement(widgetBase);});
        contextMenu.addElement(edit);
        this.contextEditButton = edit;
        var add = new TextButtonWidget(0,0,Text.of("Add"), false);
        add.setPressFunction((a) -> {openElementList(a); contextMenu.close(); elementList.childToAddTo = null;});
        contextMenu.addElement(add);
        this.contextAddButton = add;
        var addChild = new TextButtonWidget(0,0,Text.of("Add Child"), false);
        addChild.setPressFunction((a) -> {openElementList(a); contextMenu.close(); elementList.childToAddTo = selected_element;});
        contextMenu.addElement(addChild);
        this.contextChildButton = addChild;
        var delete = new TextButtonWidget(0,0,Text.of("Delete"), false);
        delete.setPressFunction((a) -> {deleteSelectedElement(a); contextMenu.close();});
        contextMenu.addElement(delete);
        this.contextDeleteButton = delete;

    }

    private void addDefaultElements() {
        elementList.registerElement("Nine Patch Texture", this::createNinePatch);
        elementList.registerElement("Texture", this::createTexture);
        elementList.registerElement("List Container", this::createListContainer);
        elementList.registerElement("Player Inventory", this::createPlayerInventory);

        elementList.registerElement("Range Texture", this::createRangeTexture);
        elementList.registerElement("Radial Texture", this::createRadialTexture);
        elementList.registerElement("Spinbox", this::createSpinbox);
        elementList.registerElement("Item Display", this::createItemDisplay);
        elementList.registerElement("Slot Display", this::createItemDisplay);

        elementList.registerElement("Color Picker", this::createColorPicker);
        elementList.registerElement("Scrollbar", this::createScrollbar);
        elementList.registerElement("Text Field", this::createTextField);
        elementList.registerElement("Text", this::createText);
    }

    private <W extends GuiButton> void openElementList(W widget) {
        propertyEditor.unloadProperties();
        elementList.open();
    }

    private void initFileTab() {
        var file = new DropDownWidget(4,4,Text.of("File"), false);
        var save = new TextButtonWidget(4,4,Text.of("Save"), false);
        file.setSpacing(4);

        file.addEntry(save);
        save.setPressFunction(GuiEditorScreen::tstfunc);
        this.file = file;
        
        addDrawableChild(file);
        addDrawableChild(save);
    }

    private void initWidgetTree() {
        var tree = new WidgetTree(4,16, 128, MinecraftClient.getInstance().getWindow().getScaledHeight() - 16, false);
        addDrawableChild(tree);
        tree.screen = this;
        this.tree = tree;
    }

    public String getExportString(String className) {
        if (children().isEmpty()) {
            return "";
        }
        return  elementImport +
                jomlImport +
                identifierImport +
                guiScreenImport +
                String.format(classString, className) +
                getContentsExportString() + "}";
    }

    public String getContentsExportString() {

        usedNames.clear();
        StringBuilder fields = new StringBuilder();
        StringBuilder assigners = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (Element element1 : children()) {
            if (element1 instanceof GuiElement guiElement) {
                if (guiElement.isEditMode()) {
                    var actual_name = guiElement.name;
                    if (usedNames.containsKey(guiElement.name)) {
                        actual_name = guiElement.name + usedNames.getOrDefault(guiElement.name, 1);
                        usedNames.put(guiElement.name, usedNames.getOrDefault(guiElement.name, 1) + 1);
                        warn(1, guiElement, guiElement.name, actual_name);
                    } else if (guiElement.name.isEmpty()) {
                        warn(0, guiElement);
                        continue;
                    } else {

                        usedNames.put(guiElement.name, 0);
                    }
                    fields.append(String.format(fieldText, guiElement.getClass().getSimpleName() + " " + actual_name));
                    assigners.append(String.format(fieldAssignerText, actual_name, guiElement.getClass().getSimpleName(), guiElement.getAssignerInitInputString(), guiElement.getAssignerSetterString()));
                    getters.append("this.").append(actual_name).append(", ");
                }
            }
        }
        getters.replace(getters.length() - 2, getters.length(), "");
        return result.append(fields).append("\n").append(String.format(initText, assigners)).append(String.format(getText, getters)).toString();
    }



}
