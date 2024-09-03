package org.shadowmaster435.gooeyeditor;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.shadowmaster435.gooeyeditor.block.TestBlock;
import org.shadowmaster435.gooeyeditor.block.TestBlockEntity;
import org.shadowmaster435.gooeyeditor.screen.GuiScreen;

import java.util.HashMap;
import java.util.logging.Logger;

public class GooeyEditor implements ModInitializer {
    public static final String id = "gooeyeditor";
    public static final Logger logger = Logger.getLogger("Gooey Editor");
    private static final HashMap<String, Class<? extends GuiScreen>> loadableScreens = new HashMap<>();
    public static ScreenHandlerType<TestHandler> TESTHANDLERTYPE;
    private static HashMap<String, Class<?>> classMap = new HashMap<>();

    public static final TestBlock TESTBLOCK = register(
            new TestBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS)),
            "testblock",
            true
    );
    public static <T extends BlockEntityType<?>> T registerEnt(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("tutorial", path), blockEntityType);
    }

    public static final BlockEntityType<TestBlockEntity> TESTBLOCKENT = registerEnt(
            "testblockent",
            BlockEntityType.Builder.create(TestBlockEntity::new, TESTBLOCK).build()
    );

    public static <B extends Block> B register(B block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = Identifier.of(GooeyEditor.id, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
    @Override
    public void onInitialize() {
        TESTHANDLERTYPE = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(id, "test"), new ScreenHandlerType<>(TestHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));
    }
    public static void warn(int warning, Object... extra_data) {
        switch (warning) {
            case 0 -> GooeyEditor.logger.warning("Tried to export a " + extra_data[0].getClass().getSimpleName() + " with a blank name. Skipped export of element.");
            case 1 -> GooeyEditor.logger.warning("Found element of type " + extra_data[0].getClass().getSimpleName() + " with a duplicate name of '" + extra_data[1] + "' exporting with name '" + extra_data[2] + "' instead.");
        }
    }

    public static Class<? extends GuiScreen> getClassForDisplayName(String display_name) {
        return loadableScreens.getOrDefault(display_name, null);
    }

    public static void registerScreenForEditor(String display_name, Class<? extends GuiScreen> screen) {
        GooeyEditor.loadableScreens.put(display_name, screen);
    }

    public static Class<?> getClassFromString(String s) {
        System.out.println("You still need to do element classMap registering boi");
        return classMap.getOrDefault(s, null);
    }

}
