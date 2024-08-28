package org.shadowmaster435.gooeyeditor.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;

public abstract class GuiScreenHandler extends ScreenHandler {



    protected GuiScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public Slot addSlot(Slot slot) {
        return super.addSlot(slot);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {

        if (button >= 100) {
            super.onSlotClick(slotIndex, button - 100, actionType, player);
        }
    }
}
