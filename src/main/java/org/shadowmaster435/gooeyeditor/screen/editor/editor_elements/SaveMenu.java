package org.shadowmaster435.gooeyeditor.screen.editor.editor_elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.editor.GuiEditorScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.*;
import org.shadowmaster435.gooeyeditor.screen.elements.container.PopupContainer;


// first element made with the editor itself :D
public class SaveMenu extends PopupContainer {

    public NinePatchTexture bg;
    public TextField classNameField;
    public TextWidget classNameText;
    public TextButtonWidget jsonSave;
    public TextButtonWidget codeSave;
    public GuiEditorScreen editorScreen;


    public SaveMenu(GuiEditorScreen editorScreen) {
        super(MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), false);
        this.editorScreen = editorScreen;
        initElements();
    }




    public void initElements() {
        NinePatchTexture bg = new NinePatchTexture(114, 99, false);
        bg.texture_width = 16;
        bg.texture_height = 16;
        bg.edge_thickness = 5;
        bg.texture = Identifier.of("gooeyeditor:textures/gui/gray_filled_white_box.png");
        bg.name = "bg";
        bg.setSize(new Vector2i(199, 43));
        bg.setScale(new Vector2f(1.0F, 1.0F));
        bg.rotation = 0.0F;
        bg.layer = 513;
        bg.setOrigin(new Vector2i(0, 0));
        bg.center_origin = false;
        bg.center_screen = true;
        addElement(bg);
        this.bg = bg;
        TextField classNameField = new TextField(177, 104, false);
        classNameField.setText("");
        classNameField.setPlaceholder("");
        classNameField.name = "classNameField";
        classNameField.setSize(new Vector2i(132, 18));
        classNameField.setScale(new Vector2f(1.0F, 1.0F));
        classNameField.rotation = 0.0F;
        classNameField.layer = 514;
        classNameField.setOrigin(new Vector2i(0, 0));
        classNameField.center_origin = false;
        classNameField.center_screen = false;
        addElement(classNameField);
        this.classNameField = classNameField;
        TextWidget classNameText = new TextWidget(120, 109, false);
        classNameText.r = 255;
        classNameText.g = 255;
        classNameText.b = 255;
        classNameText.a = 255;
        classNameText.text = "Class Name";
        classNameText.draw_shadow = false;
        classNameText.name = "classNameText";
        classNameText.setSize(new Vector2i(55, 78));
        classNameText.setScale(new Vector2f(1.0F, 1.0F));
        classNameText.rotation = 0.0F;
        classNameText.layer = 514;
        classNameText.setOrigin(new Vector2i(0, 0));
        classNameText.center_origin = false;
        classNameText.center_screen = false;
        addElement(classNameText);
        this.classNameText = classNameText;
        TextButtonWidget jsonSave = new TextButtonWidget(131, 128, false);

        jsonSave.text = "Save As Json";
        jsonSave.name = "jsonSave";
        jsonSave.setSize(new Vector2i(73, 30));
        jsonSave.setScale(new Vector2f(1.0F, 1.0F));
        jsonSave.rotation = 0.0F;
        jsonSave.layer = 514;
        jsonSave.setOrigin(new Vector2i(0, 0));
        jsonSave.center_origin = false;
        jsonSave.center_screen = false;
        addElement(jsonSave);
        this.jsonSave = jsonSave;
        TextButtonWidget codeSave = new TextButtonWidget(230, 128, false);

        codeSave.text = "Export code";
        codeSave.name = "codeSave";
        codeSave.setSize(new Vector2i(62, 30));
        codeSave.setScale(new Vector2f(1.0F, 1.0F));
        codeSave.rotation = 0.0F;
        codeSave.layer = 514;
        codeSave.setOrigin(new Vector2i(0, 0));
        codeSave.center_origin = false;
        codeSave.center_screen = false;
        addElement(codeSave);

        this.codeSave = codeSave;

        this.codeSave.setPressFunction((a) -> {editorScreen.saveCode(this.classNameField.text);close(); editorScreen.saveMenuOpen = false;});
        this.jsonSave.setPressFunction((a) -> {editorScreen.saveJson(); close(); editorScreen.saveMenuOpen = false;});
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            editorScreen.saveMenuOpen = false;
            close();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}
