package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class NumberFieldWidget extends TextField {


    private final ArrayList<Character> number_chars = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.'));



    public NumberFieldWidget(int x, int y, int w, int h, TextRenderer textRenderer, boolean editMode) {
        super(x, y, w, h, textRenderer, editMode);
        this.textPredicate = chr -> {
            if (chr.isEmpty()) {
                return true;
            } else {
               return number_chars.contains(chr.toCharArray()[0]);
            }
        };
    }

    public NumberFieldWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    public long getLong() {
        long result = Long.MAX_VALUE;
        try {result = Integer.parseInt(text);} catch (Exception ignored) {}
        try {result = (long) Float.parseFloat(text);} catch (Exception ignored) {}
        try {result = (long) Double.parseDouble(text);} catch (Exception ignored) {}
        return result;
    }

    public double getDouble() {
        double result = Double.POSITIVE_INFINITY;
        try {result = Integer.parseInt(text);} catch (Exception ignored) {}
        try {result = Float.parseFloat(text);} catch (Exception ignored) {}
        try {result = Double.parseDouble(text);} catch (Exception ignored) {}
        return result;
    }

    public float getFloat() {
        float result = Float.POSITIVE_INFINITY;
        try {result = Integer.parseInt(text);} catch (Exception ignored) {}
        try {result = Float.parseFloat(text);} catch (Exception ignored) {}
        try {result = (float) Double.parseDouble(text);} catch (Exception ignored) {}
        return result;
    }


    public int getInt() {
        int result = Integer.MAX_VALUE;
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
            } else if (StringHelper.isValidChar(chr) && number_chars.contains(chr)) {
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
            if (!textPredicate.test(string2)) {
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
