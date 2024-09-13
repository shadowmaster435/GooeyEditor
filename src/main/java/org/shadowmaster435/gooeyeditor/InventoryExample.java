package org.shadowmaster435.gooeyeditor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;
import org.shadowmaster435.gooeyeditor.screen.elements.PlayerInventoryWidget;
import org.shadowmaster435.gooeyeditor.screen.elements.SlotGridWidget;

public class InventoryExample extends GuiScreen<InventoryExampleHandler> {
    public PlayerInventoryWidget playerInventory;
    public SlotGridWidget slotGrid;

    public InventoryExample(InventoryExampleHandler handler, PlayerInventory inventory, Text text) {
        super(handler, inventory);
    }


    @Override
    public void clinit() {
        playerInventory.setHotbar(handler.getHotbarSlots());
        playerInventory.setPlayerInventory(handler.getPlayerInventorySlots());
        slotGrid.setSlots(handler.getSlots(0, 3));
    }

    public void initElements() {
        if (client == null) {
            return;
        }
        PlayerInventoryWidget playerInventory = new PlayerInventoryWidget(126, 111, false);
        playerInventory.slotWidth = 18;
        playerInventory.slotHeight = 18;
        playerInventory.name = "playerInventory";
        playerInventory.setSize(new Vector2i(162, 74));
        playerInventory.setScale(new Vector2f(1.0F, 1.0F));
        playerInventory.rotation = 0.0F;
        playerInventory.layer = 1;
        playerInventory.setOrigin(new Vector2i(0, 0));
        playerInventory.center_origin = false;
        playerInventory.center_screen = false;
        addElement(playerInventory);
        this.playerInventory = playerInventory;
        SlotGridWidget slotGrid = new SlotGridWidget(184, 67, false);
        slotGrid.regen = false;
        slotGrid.xSize = 2;
        slotGrid.ySize = 2;
        slotGrid.slotWidth = 16;
        slotGrid.slotHeight = 16;
        slotGrid.xSpacing = 0;
        slotGrid.ySpacing = 0;
        slotGrid.name = "slotGrid";
        slotGrid.setSize(new Vector2i(32, 32));
        slotGrid.setScale(new Vector2f(1.0F, 1.0F));
        slotGrid.rotation = 0.0F;
        slotGrid.layer = 1;
        slotGrid.setOrigin(new Vector2i(0, 0));
        slotGrid.center_origin = false;
        slotGrid.center_screen = false;
        addElement(slotGrid);
        this.slotGrid = slotGrid;

    }

    @Override
    public boolean isClickOutsideBounds(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }
}