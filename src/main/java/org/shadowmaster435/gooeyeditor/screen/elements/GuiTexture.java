package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.screen.editor.util.EditorUtil;
import org.shadowmaster435.gooeyeditor.screen.elements.container.GenericContainer;
import org.shadowmaster435.gooeyeditor.screen.elements.records.NinePatchTextureData;

public class GuiTexture extends ParentableWidgetBase implements EditorUtil {

    public Identifier texture = Identifier.of("minecraft:textures/block/dirt.png");
    public int texture_width = 0;
    public int texture_height = 0;


    public GuiTexture(int x, int y, int w, int h, Identifier texture, int texture_width, int texture_height, boolean editMode) {
        super(x, y, w, h, editMode);
        this.texture = texture;
        this.texture_width = texture_width;
        this.texture_height = texture_height;
//        var a = createNinePatch();
//        var b = createNinePatch();
//        a.name = "4321";
//        b.name = "498";
//      //  addElement(createNinePatch());
//        addElement(a);
//        a.addElement(b);
//        b.addElement(createNinePatch());
//        addElement(createNinePatch());
//        var a1 = createNinePatch();
//        var b1 = createNinePatch();
//        a1.name = "4322231";
//        b1.name = "49322138";
//     //   addElement(createNinePatch());
//        addElement(a1);
//    //    addElement(createNinePatch());
//
//        a1.addElement(b1);
//        b1.addElement(createNinePatch());
//        addElement(createNinePatch());
//        setX(64);
//        //   addElement(createNinePatch());
    }

    public GuiTexture(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        drawTexture(context, texture, texture_width, texture_height);
      //  System.out.println(parent + "|" + this + "|" + name);
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var texture_width = new Property("Texture Width", "texture_width", "texture_width", Integer.class);
        var texture_height = new Property("Texture Height", "texture_height", "texture_height", Integer.class);
        var texture = new Property("Texture", "texture", "texture", Identifier.class);

        return new Property[]{texture_width, texture_height, texture};
    }
}
