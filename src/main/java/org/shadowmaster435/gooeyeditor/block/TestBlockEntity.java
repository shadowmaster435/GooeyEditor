package org.shadowmaster435.gooeyeditor.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.shadowmaster435.gooeyeditor.GooeyEditor;
import org.shadowmaster435.gooeyeditor.TestHandler;
import org.shadowmaster435.gooeyeditor.util.ImplementedInventory;

public class TestBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private int size = 0;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    public PropertyDelegate slotCount = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return inventory.size();
        }

        @Override
        public void set(int index, int value) {

        }

        @Override
        public int size() {
            return 1;
        }
    };


    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(GooeyEditor.TESTBLOCKENT, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, TestBlockEntity blockEntity) {
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    // These Methods are from the NamedScreenHandlerFactory Interface
    // createMenu creates the ScreenHandler itself
    // `getDisplayName` will Provide its name which is normally shown at the top

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        // We provide *this* to the screenHandler as our class Implements Inventory
        // Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new TestHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        // for 1.19+
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
        // for earlier versions
        // return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);

        this.markDirty();
    }

    // For the following two methods, for earlier versions, remove the parameter `registryLookup`.
    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }

}
