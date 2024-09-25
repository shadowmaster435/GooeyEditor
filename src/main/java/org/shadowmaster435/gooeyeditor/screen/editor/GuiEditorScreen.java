package org.shadowmaster435.gooeyeditor.screen.editor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.*;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.action.editor.ActionBuffer;
import org.shadowmaster435.gooeyeditor.screen.elements.action.editor.EditorAction;
import org.shadowmaster435.gooeyeditor.screen.elements.container.BaseContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.DropdownContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.container.ListContainer;
import org.shadowmaster435.gooeyeditor.util.ClassCodeStringBuilder;
import org.shadowmaster435.gooeyeditor.util.InputHelper;
import org.shadowmaster435.gooeyeditor.util.SimpleFileDialogue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.shadowmaster435.gooeyeditor.GooeyEditor.warn;

public class GuiEditorScreen extends Screen implements EditorUtil {

    public static final int EDITOR_LAYERS_START = 512;
    public final ArrayList<SealedGuiElement> toAdd = new ArrayList<>();
    public final ArrayList<SealedGuiElement> toRemove = new ArrayList<>();

    public final HashMap<SealedGuiElement, GuiElement> toAddToChild = new HashMap<>();
    private boolean didInit = false;
    private final Screen parent;
    private final HashMap<String, Integer> usedNames = new HashMap<>();
    public ButtonWidget button1;
    public SealedGuiElement element;
    public ButtonWidget dragged_button;
    public TextureButtonWidget layer_editing;
    public TextureButtonWidget showContainers;
    public WidgetTree tree;
    private TextButtonWidget file;
    private TextButtonWidget save;
    private TextButtonWidget color_rect;
    private SealedGuiElement selected_element = null;
    private IdentifierWidget w1;
    private PropertyEditor propertyEditor;
    private TextButtonWidget add;
    private ElementList elementList;
    private SpinboxWidget displayed_layer;
    private ContextPopupWidget contextMenu;
    private TextButtonWidget contextEditButton;
    private TextButtonWidget contextDeleteButton;
    private TextButtonWidget contextAddButton;
    private TextButtonWidget centerElementButton;
    public boolean saveMenuOpen = false;
    public SaveMenu saveMenu;
    public ActionBuffer<EditorAction> actionBuffer = new ActionBuffer<>();

    private TextButtonWidget contextChildButton;
    private final ArrayList<SealedGuiElement> elements = new ArrayList<>();

    public GuiEditorScreen(Screen screen) {
        super(Text.empty());
        this.parent = screen;
        GooeyEditorClient.currentEditor = this;
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

    public SealedGuiElement getSelectedElement() {
        return selected_element;
    }

    @Override
    public void close() {
        super.close();
        GooeyEditorClient.currentEditor = null;
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
                if (element1 instanceof SealedGuiElement element2) {
                    if (element2 instanceof GuiElement widgetBase) {
                        AtomicBoolean stop = new AtomicBoolean(false);

                        widgetBase.forEachInBranch((element3, parent, d) -> {

                            if (!stop.get()) {
                                if (element3.isMouseOver(mouseX, mouseY) && element3.isEditMode() && element3.selectable && isElementLayerVisible(element3) && !hoveringPropertyEditor) {
                                    selectElement(element3);
                                    stop.set(true);
                                }
                            }
                        }, 0);
                        if (stop.get()) {
                            return super.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                    if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && element2.selectable && isElementLayerVisible(element2)) {
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

                    if (element1 instanceof SealedGuiElement element2) {
                        if (element2 instanceof GuiElement widgetBase) {
                            AtomicBoolean stop = new AtomicBoolean(false);
                            widgetBase.forEachInBranch((element3, parent, d) -> {
                                if (!stop.get()) {
                                    if (element3.isMouseOver(mouseX, mouseY) && element3.isEditMode() && element3.selectable && isElementLayerVisible(element3) && !hoveringPropertyEditor) {
                                        selectElement(element3);
                                        stop.set(true);
                                    }
                                }
                            }, 0);
                            if (stop.get()) {
                                return super.mouseClicked(mouseX, mouseY, button);
                            }
                        }
                        if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && element2.selectable && isElementLayerVisible(element2)) {
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

    public boolean hasSelectedElement() {
        return selected_element != null;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    /**
     * Sets selection to provided element and marks previously selected element (if any) as unselected.
     * @param element Element you want selected.
     */
    public void selectElement(SealedGuiElement element) {

        if (selected_element != null) { // unselect previous if not null
            selected_element.selected = false;
        }
        selected_element = element;
        selected_element.selected = true;
        propertyEditor.loadProperties(selected_element, true);
    }
    /**
     * Sets selection to provided element and marks previously selected element (if any) as unselected.
     * @param element Element you want selected.
     * @param genTree If false keeps the previously generated tree.
     */
    public void selectElement(SealedGuiElement element, boolean genTree) {

        if (selected_element != null) { // unselect previous if not null
            selected_element.selected = false;
        }
        selected_element = element;
        selected_element.selected = true;
        propertyEditor.loadProperties(selected_element, genTree);
    }

    public ArrayList<SealedGuiElement> getElements() {
        return new ArrayList<>(elements);
    }

    private boolean isElementLayerVisible(SealedGuiElement element) {
        return !layer_editing.pressed || element.layer == displayed_layer.getInt();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var children = new ArrayList<>(children());
        for (Element element1 : children) {
            if (element1 == null) {
                continue;
            }
            element1.keyPressed(keyCode, scanCode, modifiers);
        }
        if (selected_element != null && (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) && !propertyEditor.isPropertyFocused()) {
            deleteSelectedElement(null);
        }
        if (keyCode == GLFW.GLFW_KEY_Z) {
            if (modifiers == 2) {
                actionBuffer.undo();
            } else if (modifiers == 3) {
                actionBuffer.redo();
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void deleteSelectedElement(GuiButton button) {
        toRemove.add(selected_element);
        selected_element = null;

    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Element element1 : children()) {
            element1.charTyped(chr, modifiers);
        }
        if (InputHelper.isMiddleMouseHeld) {
           loadScreen("InventoryExample");
           // System.out.println(getExportString("InventoryExample"));
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
            if (par instanceof GuiElement p) {
                p.addElement(elem);
            }
        });
        toRemove.forEach((e) -> {
            remove(e);

            if (!children().contains(e) && e.parent instanceof GuiElement p) {
                p.removeElement(e);
            }
            elements.remove(e);
            propertyEditor.unloadProperties();
        });
        toAddToChild.clear();
        toAdd.clear();
        toRemove.clear();
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
        var tre = tree.editorRect.contains(mouseX,mouseY) && isPropertyEditorOpen();
        return pEdit || ctx || tre || elementList.isOpen() || saveMenuOpen;
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
                    if ((!c.isEditMode() && !c.selected) || !c.selectable) {
                        continue;
                    }
                    context.fill(c.getGlobalX(), c.getGlobalY(), c.getGlobalX() + c.getWidth(), c.getGlobalY() + c.getHeight(), c.layer + 1, ColorHelper.Argb.getArgb(255,255,255,255));
                } else if (e instanceof GuiElement w) {
                    w.forEachInBranch((a, par, d) -> {
                        if ((a instanceof BaseContainer && a.isEditMode() && !a.selected) || !a.selectable) {
                            context.fill(a.getGlobalX(), a.getGlobalY(), a.getGlobalX() + a.getWidth(), a.getGlobalY() + a.getHeight(), a.layer + 1, ColorHelper.Argb.getArgb(255, 255, 255, 255));
                        }
                    }, 0);
                }
            }
        }
    }

    @Override
    protected void init() {
        if (saveMenuOpen) {
            var txt = saveMenu.classNameField.text;
            openSaveMenu();
            saveMenu.classNameField.text = txt;
        }
        if (didInit) {
            var elements = new ArrayList<>(this.elements);
            this.elements.clear();
            for (SealedGuiElement e : elements) {
                addDrawableChild(e);
            }

            addDrawableChild(this.propertyEditor);
            this.propertyEditor.align();

            initTopBar();
            initContextMenu();
            tree.createTreeForElement((GuiElement) selected_element);

        } else {
            var editor = new PropertyEditor(0, 32, false);
            addDrawableChild(editor);
            this.propertyEditor = editor;
            didInit = true;
            initTopBar();
            initContextMenu();
            didInit = true;
        }

    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        if (drawableElement instanceof SealedGuiElement e && e.isEditMode()) {
            elements.add(e);
        }
        return super.addDrawableChild(drawableElement);
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
        var add = new TextButtonWidget(32,4,("Add"), false);
        var elementList = new ElementList(0,0,256, 192, this, false);
        add.setPressFunction(this::openElementList);
        addDrawableChild(add);
        this.add = add;
        this.elementList = elementList;
        addDrawableChild(elementList);
        addDefaultElements();
        addElements();
    }

    private void initContextMenu() {

        var contextMenu = new ContextPopupWidget(128, 96, false);
        contextMenu.layer = 600;
        this.contextMenu = contextMenu;
        addDrawableChild(contextMenu);
        var edit = new TextButtonWidget(0,0,("Edit"), false);
        edit.setPressFunction((a) -> {propertyEditor.renderText(true); contextMenu.close(); if (selected_element instanceof GuiElement widgetBase) tree.createTreeForElement(widgetBase);});
        contextMenu.addElement(edit);
        this.contextEditButton = edit;
//        var center = new TextButtonWidget(0,0,Text.of("Center Element"), false);
//        center.setPressFunction((a) -> {
//            centerElement(selected_element);
//            contextMenu.close();
//        });
//        contextMenu.addElement(center);
//        this.centerElementButton = center;
        var add = new TextButtonWidget(0,0,("Add"), false);
        add.setPressFunction((a) -> {openElementList(a); contextMenu.close(); elementList.childToAddTo = null;});
        contextMenu.addElement(add);
        this.contextAddButton = add;
        var addChild = new TextButtonWidget(0,0,"Add Child", false);
        addChild.setPressFunction((a) -> {openElementList(a); contextMenu.close(); elementList.childToAddTo = selected_element;});
        contextMenu.addElement(addChild);
        this.contextChildButton = addChild;
        var delete = new TextButtonWidget(0,0,("Delete"), false);
        delete.setPressFunction((a) -> {deleteSelectedElement(a); contextMenu.close();});
        contextMenu.addElement(delete);
        this.contextDeleteButton = delete;

    }

    private void addDefaultElements() {
        elementList.registerElement("Nine Patch Texture", this::createNinePatch);
        elementList.registerElement("Texture", this::createTexture);
        elementList.registerElement("List Container", this::createListContainer);
        elementList.registerElement("Slot Grid", this::createSlotGrid);
        elementList.registerElement("Player Inventory", this::createPlayerInventory);
        elementList.registerElement("Range Texture", this::createRangeTexture);
        elementList.registerElement("Radial Texture", this::createRadialTexture);
        elementList.registerElement("Spinbox", this::createSpinbox);
        elementList.registerElement("Item Display", this::createItemDisplay);
        elementList.registerElement("Slot Display", this::createSlotDisplay);
        elementList.registerElement("Box Container", this::createBoxContainer);
        elementList.registerElement("Tab Container", this::createTabContainer);
        elementList.registerElement("Page Container", this::createPaginatedContainer);
        elementList.registerElement("Paged List Container", this::createPaginatedListContainer);
        elementList.registerElement("Scrollbar", this::createScrollbar);
        elementList.registerElement("Text", this::createText);
        elementList.registerElement("Text Field", this::createTextField);
        elementList.registerElement("Text Button", this::createTextButton);
        elementList.registerElement("Nine Patch Button", this::createNinePatchButton);
        elementList.registerElement("Texture Button", this::createTextureButton);
        elementList.registerElement("Toggle Container", this::createToggleContainer);
        elementList.registerElement("Container", this::createGenericContainer);

    }

    public void addElements() {
        var elements = GooeyEditor.getRegisteredElements();
        elements.forEach((id, func) -> elementList.registerElement(GooeyEditor.getElementDisplayName(id), func));
    }

    private <W extends GuiButton> void openElementList(W widget) {
        propertyEditor.unloadProperties();
        elementList.open();
    }

    private SimpleFileDialogue dialogue;

    private void initFileTab() {
        var dropDown = new DropdownContainer(2, 8, 10, 10, false);
        var list = new ListContainer(0,0,0,0,2,false);
        var file = new TextButtonWidget(4,4,"File", false);
        var load = new TextButtonWidget(0,0, "Load", false);
        var save = new TextButtonWidget(0,0, "Save", false);
        var exit = new TextButtonWidget(0,0, "Exit", false);
        dropDown.close();
       // var save = new TextButtonWidget(4,4,Text.of("Save"), false);
      //  file.setSpacing(4);

     //   file.addEntry(save);
        file.setPressFunction((a) -> {if (!saveMenuOpen) dropDown.toggle();});
        load.setPressFunction((a) -> {if (!saveMenuOpen) {fromJson(loadJson()); dropDown.close();}});
        save.setPressFunction((a) -> {if (!saveMenuOpen) {openSaveMenu(); dropDown.close();}});
        exit.setPressFunction((a) -> {if (!saveMenuOpen){close();}});
        this.file = file;

        
        addDrawableChild(file);
        addDrawableChild(dropDown);
        dropDown.addElement(list);
        list.addElements(save, load, exit);
    }

    private void openSaveMenu() {
        saveMenuOpen = true;
        var menu = new SaveMenu(this);
        toAdd.add(menu);
        this.saveMenu = menu;

        saveMenu.open(0,0);
    }



    /**
     * Opens a file dialogue window to select a json to load.
     * @return Selected json as a {@link JsonObject}. Returns an empty {@link JsonObject} if an error occurs.
     */
    public JsonObject loadJson() {
        var path = SimpleFileDialogue.open(".json", "Json Files", false);
        var result = new JsonObject();
        if (path == null) return result;
        try {
            if (new File(path).exists()) {
                var file = new FileInputStream(path);
                var s = new String(file.readAllBytes());
                var json = JsonParser.parseString(s);
                result = json.getAsJsonObject();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * Opens a file dialogue window to select a location to save the exported json.
     */
    public void saveJson() {
        var json = toJson();
        SimpleFileDialogue.save(json, ".json", "Json Files");
    }

    public void removeElement(SealedGuiElement element) {
        toRemove.add(element);
    }

    public void saveCode(String className) {
        SimpleFileDialogue.save(getExportString(className), ".txt", "Text Document");
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
        return getContentsExportString(className);
//        return  elementImport +
//                jomlImport +
//                identifierImport +
//                guiScreenImport +
//                String.format(classString, className) +

    }

    public String getContentsExportString(String className) {
        var code = new ClassCodeStringBuilder(className, HandledGuiScreen.class);
        var setVarsMethod = new ClassCodeStringBuilder.MethodStringBuilder("initElements", null, null, false);
        var initMethod = new ClassCodeStringBuilder.MethodStringBuilder(className, null, null, true);
        usedNames.clear();

        for (Element element1 : children()) {
            if (element1 instanceof GuiElement guiElement) {
                if (guiElement.isEditMode() && guiElement.needsExport) {
                    var actual_name = getSafeName(guiElement, usedNames, true);

                    guiElement.createLocalInitString(setVarsMethod, guiElement.getClass(), actual_name);
                    guiElement.createAssignerSetterString(setVarsMethod, actual_name, usedNames, true);
                    guiElement.createAssignerInitInputString(setVarsMethod, guiElement.getClass(), actual_name);

                    code.field(new ClassCodeStringBuilder.FieldStringBuilder(guiElement.getClass(), actual_name));
                    guiElement.forEachInBranch((e, p, d) -> {
                        if (e.needsExport) {
                            var actual_child_name = getSafeName(e, usedNames, true);
                            code.field(new ClassCodeStringBuilder.FieldStringBuilder(e.getClass(), actual_child_name));
                        }
                    }, 0);
                }
            }
        }
        initMethod.line("initElements();");
        code.method(initMethod).method(setVarsMethod);
        return code.build();
    }

    public static String getSafeName(SealedGuiElement guiElement, HashMap<String, Integer> usedNames, boolean add) {
        var actual_name = guiElement.name;
        if (usedNames.containsKey(guiElement.name)) {
            if (add) {
                actual_name = guiElement.name + usedNames.getOrDefault(guiElement.name, 1);

                usedNames.put(guiElement.name, usedNames.getOrDefault(guiElement.name, 1) + 1);
              warn(1, guiElement, guiElement.name, actual_name);
            } else {
                var safe =  guiElement.name + (usedNames.getOrDefault(guiElement.name, 1) - 1);
                if (safe.equals(guiElement.name + "-1")) {
                    safe = guiElement.name;
                }
                actual_name = safe;
            }
        } else if (guiElement.name.isEmpty()) {
            if (add) {
                warn(0, guiElement);
            }
            return null;
        } else {
            if (add) {
                usedNames.put(guiElement.name, 0);
            }
        }
        return actual_name;
    }

    public final void fromJson(JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            var element = SealedGuiElement.fromJson(entry.getValue().getAsJsonObject(), entry.getKey(), true);
            toAdd.add(element);
        }
    }

    public String toJson() {
        var json = new JsonObject();
        for (Element element1 : children()) {
            if (element1 instanceof SealedGuiElement guiElement && guiElement.isEditMode() && ((SealedGuiElement) element1).needsExport) {
                guiElement.writeJson(json);
            }
        }
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setLenient(true);
            jsonWriter.setIndent("\t");
            Streams.write(json, jsonWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
