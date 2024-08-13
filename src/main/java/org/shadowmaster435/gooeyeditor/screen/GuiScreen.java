package org.shadowmaster435.gooeyeditor.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GuiScreen extends Screen {

    private final HashMap<String, ? extends GuiElement> elements = new HashMap<>();

    protected GuiScreen() {
        super(Text.of(""));
    }
    public abstract void initElements();
    public abstract GuiElement[] getElements();

}
