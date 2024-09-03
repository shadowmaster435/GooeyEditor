package org.shadowmaster435.gooeyeditor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.client.GooeyEditorClient;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;
import org.shadowmaster435.gooeyeditor.screen.GuiScreenHandler;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotGridWidget;

import java.util.Optional;

public class TestHandler extends GuiScreenHandler {
    public Inventory inv;
    public PlayerInventory playerInventory;

    public TestHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) { // server
        super(GooeyEditor.TESTHANDLERTYPE, syncId);
        this.inv = inventory;
        this.playerInventory = playerInventory;
        for (int i = 0; i < 4; ++i) {
            addSlot(new Slot(inventory, i, -1000, -1000));
        }
    }
    public TestHandler(int syncId, PlayerInventory inventory) { // client
        this(syncId, inventory, new SimpleInventory(4));
    }




    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inv.canPlayerUse(player);
    }

}