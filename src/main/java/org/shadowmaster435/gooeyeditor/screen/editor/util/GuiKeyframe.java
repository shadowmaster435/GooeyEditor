package org.shadowmaster435.gooeyeditor.screen.editor.util;

import org.jetbrains.annotations.Nullable;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;

public class GuiKeyframe<T> {

    private GuiKeyframe<T> previous;
    private GuiKeyframe<T> next;
    private final float time = 0;
    private Easing easing = Easing.LINEAR;

    public GuiKeyframe(SealedGuiElement.Property property, float time) {

    }

    public GuiKeyframe<T> getNext() {
        return (next != null) ? next : this;
    }

    public GuiKeyframe<T> getPrevious() {
        return (previous != null) ? previous : this;
    }

    public void setNext(@Nullable GuiKeyframe<T> next) {
        this.next = next;
    }

    public void setPrevious(@Nullable GuiKeyframe<T> previous) {
        this.previous = previous;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }

    public float easeDelta(float interp_time, GuiKeyframe<T> tGuiKeyframe) {
        var duration = Math.abs(Math.min(tGuiKeyframe.time, time) - Math.max(tGuiKeyframe.time, time));
        return easing.ease(interp_time, Math.min(tGuiKeyframe.time, time), Math.max(tGuiKeyframe.time, time), duration);
    }

}
