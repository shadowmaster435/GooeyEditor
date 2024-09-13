package org.shadowmaster435.gooeyeditor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.GuiScreenHandler;

import java.util.ArrayList;

public class InventoryExampleHandler extends GuiScreenHandler {
    public ArrayList<Slot> pinv;

    public InventoryExampleHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) { // server
        super(GooeyEditor.TESTHANDLERTYPE, syncId);
        this.playerInventory = playerInventory;
        this.inventory = inventory;
        for (int i = 0; i < 4; ++i) {
            addSlot(new Slot(inventory, i, -1000, -1000));
        }
        addSlots(createSlotsForHotbar(playerInventory));
        addSlots(createSlotsForPlayerInventory(playerInventory));

    }
    public InventoryExampleHandler(int syncId, PlayerInventory inventory) { // client
        this(syncId, inventory, new SimpleInventory(4));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

}