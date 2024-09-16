package org.shadowmaster435.gooeyeditor.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public abstract class GuiScreenHandler extends ScreenHandler {

    private final ArrayList<Slot> hotbarSlots = new ArrayList<>();
    private final ArrayList<Slot> playerInventorySlots = new ArrayList<>();
    public PlayerInventory playerInventory;
    public Inventory inventory;

    protected GuiScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }


    public void addSlots(Collection<Slot> slots) {
        for (Slot slot : slots) {
            addSlot(slot);
        }
    }

    public void addSlots(Slot... slots) {
        for (Slot slot : slots) {
            addSlot(slot);
        }
    }

    /**
     * @return Slot with provided id.
     */
    public Slot createSlot(Inventory inventory, int id) {
        return new Slot(inventory, id, -1000, -1000);
    }
    /**
     * @return a list of slots with ids starting at {@code min_id} and ending at {@code max_id}
     */
    public ArrayList<Slot> createSlots(Inventory inventory, int min_id, int max_id) {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = min_id; i <= max_id; ++i) {
            result.add(new Slot(inventory, i, -1000, -1000));
        }
        return result;
    }

    /**
     * @return a list of 27 slots corresponding to the 27 slots in the player inventory gui.
     */
    public ArrayList<Slot> createSlotsForPlayerInventory(PlayerInventory inventory) {
        ArrayList<Slot> result = new ArrayList<>();

        for (int i = 9; i < 36; ++i) {
            result.add(new Slot(inventory, i, -1000, -1000));
        }
        playerInventorySlots.addAll(result);
        return result;
    }
    /**
     * @return a list of 9 slots corresponding to the hotbar.
     */
    public ArrayList<Slot> createSlotsForHotbar(PlayerInventory inventory) {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            result.add(new Slot(inventory, i, -1000, -1000));
        }
        hotbarSlots.addAll(result);

        return result;
    }

    /**
     * @return Player hotbar slots.
     */
    public ArrayList<Slot> getHotbarSlots() {
        return new ArrayList<>(hotbarSlots);
    }
    /**
     * @return Player inventory slots.
     */
    public ArrayList<Slot> getPlayerInventorySlots() {
        return playerInventorySlots;
    }

    /**
     * Gets a list with the provided slot ids. Throws a {@link NullPointerException} if an id doesn't have a corresponding slot.
     * @param min_id Slot id to start at.
     * @param max_id Slot id to end at.
     * @return A list of slots with ids starting ad {@code min_id} and ending at {@code max_id}.
     */
    public ArrayList<Slot> getSlots(int min_id, int max_id) {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i = min_id; i <= max_id; ++i) {
            result.add(super.getSlot(i));
        }
        return result;
    }

    /**
     * Gets a list with the provided slot ids. Throws a {@link NullPointerException} if any of the provided ids do not have a corresponding slot.
     * @param ids Slot ids to get.
     * @return A list of slots with the corresponding provided ids.
     */
    public ArrayList<Slot> getSlots(int... ids) {
        ArrayList<Slot> result = new ArrayList<>();
        for (int i : ids) {
            result.add(getSlot(i));
        }
        return result;
    }

}
