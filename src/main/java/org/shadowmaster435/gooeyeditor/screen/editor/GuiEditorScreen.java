package org.shadowmaster435.gooeyeditor.screen.editor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.lwjgl.glfw.GLFW;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.ElementList;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.IdentifierWidget;
import org.shadowmaster435.gooeyeditor.screen.editor.editor_elements.PropertyEditor;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.util.InputHelper;

import java.util.ArrayList;

public class GuiEditorScreen extends Screen implements EditorUtil {

    public ButtonWidget button1;
    public GuiElement element;
    public ButtonWidget dragged_button;
    private final Screen parent;
    private DropDownWidget file;
    private TextButtonWidget save;
    private DropDownWidget create;
    private TextButtonWidget color_rect;
    private GuiElement selected_element = null;
    private IdentifierWidget w1;
    private PropertyEditor propertyEditor;
    private TextButtonWidget add;
    public final ArrayList<GuiElement> toAdd = new ArrayList<>();
    private ElementList elementList;
    private SpinboxWidget displayed_layer;
    private TextureButtonWidget layer_editing;

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
        if (selected_element == null) {
            for (Element element1 : children()) {
                if (element1 instanceof GuiElement element2) {
                    if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && isElementLayerVisible(element2)) {
                        selected_element = element2;
                        selected_element.selected = true;
                        propertyEditor.loadProperties(element2);

                        return super.mouseClicked(mouseX, mouseY, button);
                    }
                }
            }
        } else {
            if (selected_element.isMouseOver(mouseX, mouseY) && selected_element.selected && selected_element.isEditMode() && isElementLayerVisible(selected_element)) {
                if (button == 1) {
                    propertyEditor.renderText(true);
                }
                if (button == 0) {
                    selected_element.startTransform((int) mouseX, (int) mouseY);
                }
              //  propertyEditor.loadProperties(selected_element);

            } else {
                for (Element element1 : children()) {
                    if (element1 instanceof GuiElement element2) {

                        if (element2.isMouseOver(mouseX, mouseY) && element2.isEditMode() && isElementLayerVisible(element2)) {
                            if (element2 == selected_element) {
                                return super.mouseClicked(mouseX, mouseY, button);
                            }
                            selected_element.selected = false;
                            selected_element = element2;
                            selected_element.selected = true;
                            propertyEditor.loadProperties(selected_element);

                            return super.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                }
                if (!propertyEditor.hoveringProperty((int) mouseX, (int) mouseY)) {
                    propertyEditor.unloadProperties();

                    selected_element.selected = false;
                    selected_element = null;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
            remove(selected_element);
            selected_element = null;
            propertyEditor.unloadProperties();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Element element1 : children()) {
            element1.charTyped(chr, modifiers);
        }
        if (InputHelper.isMiddleMouseHeld) {
            System.out.println(getExportString("Test"));
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        if (dragged_button != null) {
//            dragged_button.setPosition(mouseX, mouseY);
//
//        }
//        element.rotation += 1f;
        var element_found = false;
        for (Element element1 : children()) {
            if (element1 instanceof GuiElement element2) {
                if (element2.isMouseOver(mouseX, mouseY) && isElementLayerVisible(element2)) {
                    if (element2 instanceof ElementList || element2 instanceof PropertyEditor) {
                        continue;
                    }
                    element2.setCursorType(mouseX, mouseY);
                    element_found = true;
                    break;
                }
            }
        }


        if (!element_found) {
            GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
        }

        toAdd.forEach(this::addDrawableChild);
        toAdd.clear();
        super.render(context, mouseX, mouseY, delta);
        updateProperties();
    }

    public void renderPropertyBar(DrawContext context) {
        if (selected_element != null) {
            context.fill(context.getScaledWindowWidth() - 64, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), ColorHelper.Argb.getArgb(127,127,127));
        }
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
    }

    @Override
    protected void init() {

        var editor = new PropertyEditor(0,32,false);
        addDrawableChild(editor);
        this.propertyEditor = editor;
        initTopBar();
    }

    private void initTopBar() {
        initFileTab();
        initAddTab();
        initLayerTools();
    }

    private void initLayerTools() {
        var spinbox = new SpinboxWidget(64, 2, 52, 12, MinecraftClient.getInstance().textRenderer, false);
        spinbox.layer = 513;
        spinbox.max_value = 500;
        spinbox.min_value = 1;
        spinbox.setTooltip("Enabled Layer");
        spinbox.setTooltipPos(56, 32);
        spinbox.positionToolTip(true);
        spinbox.setValue(0);
        this.displayed_layer = spinbox;
        addDrawableChild(spinbox);
        var checkbox = new TextureButtonWidget(128, 2, 12, 12, TextureButtonWidget.CHECKBOX, false);
        checkbox.layer = 513;
        checkbox.setTooltip("Layer Editing");
        checkbox.positionToolTip(true);
        checkbox.setTooltipPos(120, 32);
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

    private void addDefaultElements() {
        elementList.registerElement("Nine Patch Texture", this::createNinePatch);
        elementList.registerElement("Texture", this::createTexture);
        elementList.registerElement("Item Display", this::createItemDisplay);
        elementList.registerElement("Color Picker", this::createColorPicker);

        elementList.registerElement("Scrollbar", this::createScrollbar);
        elementList.registerElement("Text Field", this::createTextField);
        elementList.registerElement("Text", this::createText);
    }


    private <W extends GuiButton> void openElementList(W widget) {
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


    public static <W extends GuiButton> void tstfunc(W widget) {
        System.out.println("yep");
    }

    private final String fieldText = "\tpublic %s;\n";
    private final String fieldAssignerText = "\t\tvar %1$s = new %2$s(%3$s);\n\t\t%4$s\n\t\tthis.%1$s = %1$s;\n\n";
    private final String initText = "\t@Override\n\tpublic void initElements() {\n%s\t}\n";
    private final String getText = "\n\t@Override\n\tpublic GuiElement[] getElements() {\n\t\treturn new GuiElement[]{%s};\n\t}\n";
    private final String elementImport = "import org.shadowmaster435.gooeyeditor.screen.elements.*;\n";
    private final String jomlImport = "import org.joml.*;\n";
    private final String identifierImport = "import net.minecraft.util.Identifier;\n";
    private final String guiScreenImport = "import org.shadowmaster435.gooeyeditor.screen.GuiScreen;\n";

    private final String classString = "\npublic class %s extends GuiScreen {\n";

    public String getExportString(String className) {
        return  elementImport +
                jomlImport +
                identifierImport +
                guiScreenImport +
                String.format(classString, className) +
                "\n" +
                getContentsExportString() + "}";
    }

    public String getContentsExportString() {
        StringBuilder fields = new StringBuilder();
        StringBuilder assigners = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (Element element1 : children()) {
            if (element1 instanceof GuiElement guiElement) {
                if (guiElement.isEditMode()) {
                    fields.append(String.format(fieldText, guiElement.getClass().getSimpleName() + " " + guiElement.name));
                    assigners.append(String.format(fieldAssignerText, guiElement.name, guiElement.getClass().getSimpleName(), guiElement.getAssignerInitInputString(), guiElement.getAssignerSetterString()));
                    getters.append("this.").append(guiElement.name).append(", ");
                }
            }
        }
        getters.replace(getters.length() - 2, getters.length(), "");
        return result.append(fields).append("\n").append(String.format(initText, assigners)).append(String.format(getText, getters)).toString();
    }


}
