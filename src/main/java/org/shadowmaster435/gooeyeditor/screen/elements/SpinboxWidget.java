package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class SpinboxWidget extends TextField {


    private final ArrayList<Character> number_chars = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'));

    public int max_value = 100;
    public int min_value = -100;

    private final TextButtonWidget add;
    private final TextButtonWidget sub;


    @Override
    public Property[] getProperties() {
        var min_value = new Property("Min Value", "min_value", "min_value", Integer.class);
        var max_value = new Property("Max Value", "max_value", "max_value", Integer.class);

        var placeholder = new Property("Placeholder Text", "setPlaceholder", "getPlaceholder", String.class);
        return new Property[]{min_value, max_value, placeholder};
    }
    public SpinboxWidget(int x, int y, int w, int h, TextRenderer textRenderer, boolean editMode) {
        super(x, y, w, h, textRenderer, editMode);
        this.textPredicate = chr -> {
            if (chr.isEmpty()) {
                return true;
            } else {
               return number_chars.contains(chr.toCharArray()[0]);
            }
        };
        var pw = MinecraftClient.getInstance().textRenderer.getWidth("+");
        var mw = MinecraftClient.getInstance().textRenderer.getWidth("-");
        add = new TextButtonWidget(((w - 2) - pw), (h / 4), Text.of("+"), editMode);
        sub = new TextButtonWidget(((w - 2) - ((mw + pw) + 1)), (h / 4), Text.of("-"), editMode);
        add.setPressFunction(this::add);
        sub.setPressFunction(this::sub);
        addElement(sub);
        addElement(add);

    }

    public SpinboxWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        this.textPredicate = chr -> {
            if (chr.isEmpty()) {
                return true;
            } else {
                return number_chars.contains(chr.toCharArray()[0]);
            }
        };
        var pw = MinecraftClient.getInstance().textRenderer.getWidth("+");
        var mw = MinecraftClient.getInstance().textRenderer.getWidth("-");
        add = new TextButtonWidget(x + ((14) - pw), y + (4), Text.of("+"), editMode);
        sub = new TextButtonWidget(x + ((14) - ((mw + pw) + 1)), y + (4), Text.of("-"), editMode);
        add.setPressFunction(this::add);
        sub.setPressFunction(this::sub);
        addElement(sub);
        addElement(add);
    }

    public void add(GuiButton button) {
        setValue(Math.min(getInt() + 1, max_value));
    }

    public void sub(GuiButton button) {
        setValue(Math.max(getInt() - 1, min_value));

    }

    private int getIntOf(String string) {
        int result = min_value;
        try {result = Integer.parseInt(string);} catch (Exception ignored) {}
        try {result = (int) Float.parseFloat(string);} catch (Exception ignored) {}
        try {result = (int) Double.parseDouble(string);} catch (Exception ignored) {}
        try {result = (int) Long.parseLong(string);} catch (Exception ignored) {}
        return result;
    }

    public int getInt() {
        int result = min_value;
        try {result = Integer.parseInt(text);} catch (Exception ignored) {}
        try {result = (int) Float.parseFloat(text);} catch (Exception ignored) {}
        try {result = (int) Double.parseDouble(text);} catch (Exception ignored) {}
        try {result = (int) Long.parseLong(text);} catch (Exception ignored) {}
        return result;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!isEditMode()) {

            if (!this.isActive()) {
                return false;
            } else if (StringHelper.isValidChar(chr) && number_chars.contains(chr) && (intInBounds(getIntOf(getText() + chr)) || !getSelectedText().isEmpty())) {
                if (this.editable) {
                    this.write(Character.toString(chr));
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean intInBounds(int i) {
        return i <= max_value && i >= min_value;
    }

    public void setValue(Number number) {
        setText(String.valueOf(number));
    }

    public void write(String text) {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        int k = this.maxLength - this.text.length() - (i - j);
        if (k > 0) {
            String string = StringHelper.stripInvalidChars(text);
            int l = string.length();
            if (k < l) {
                if (Character.isHighSurrogate(string.charAt(k - 1))) {
                    --k;
                }

                string = string.substring(0, k);
                l = k;
            }


            String string2 = new StringBuilder(this.text).replace(i, j, string).toString();
            if (!textPredicate.test(string2) || !intInBounds(getIntOf(string2))) {
                return;
            }
            if (this.textPredicate.test(string2)) {
                this.text = string2;
                this.setSelectionStart(i + l);
                this.setSelectionEnd(this.selectionStart);
                this.onChanged(this.text);
            }
        }
    }
}
