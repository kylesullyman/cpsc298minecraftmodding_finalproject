package com.example.finalprojectmod;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
// teste comment
// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(FinalProjectMod.MODID)
public class FinalProjectMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "finalprojectmod";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Create a Deferred Register to hold Blocks which will all be registered under the "finalprojectmod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // Create a Deferred Register to hold Items which will all be registered under the "finalprojectmod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "finalprojectmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // ADD TOOL MATERIALS HERE
    public static final ToolMaterial GOD_TIER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // Least restrictive (or define your own)
            9999,   // durability
            100.0F, // speed
            25.0F,  // attack bonus
            30,     // enchantability
            ItemTags.PLANKS // Repairable with wood planks (for fun)
    );

   /* public static final DeferredBlock<Block> SPONGEBOB_BLOCK = BLOCKS.registerSimpleBlock("spongebob_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(0.6f, 1200.0f) // Hardness, Resistance
                    .lightLevel(state -> 6) // Emits light level 6
                    .sound(net.minecraft.world.level.block.SoundType.SPONGE) // Stone sponge when stepped on or broken
    );

    */

    public static final DeferredBlock<Block> SPONGEBOB_BLOCK =
            BLOCKS.register("spongebob_block", registryName ->
                    new SpongebobBlock(
                            BlockBehaviour.Properties.of()
                                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                                    .mapColor(MapColor.GOLD)
                                    .strength(0.6f, 1200.0f)
                                    .lightLevel(state -> 6)
                                    .sound(net.minecraft.world.level.block.SoundType.SPONGE)
                                    .noLootTable()
                    )
            );



    // ADD BLOCKITEMS HERE
    public static final DeferredItem<BlockItem> SPONGEBOB_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("spongebob_block",
                    SPONGEBOB_BLOCK);



    // ADD ITEMS HERE
    public static final DeferredItem<Item> SECRET_FORMULA =
            ITEMS.register("secret_formula", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    )
            );

    public static final DeferredItem<Item> SPATULA =
            ITEMS.register("spatula", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    )
            );

    public static final DeferredItem<Item> PLANKTON =
            ITEMS.register("plankton", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    )
            );

    public static final DeferredItem<Item> KRABBY_PATTY =
            ITEMS.register("krabby_patty", registryName ->
                    new Item(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                            .food(new FoodProperties.Builder()
                                    .nutrition(20)            // full hunger bar
                                    .saturationModifier(1.0f) // makes it very filling
                                    .alwaysEdible()           // can eat even at full hunger
                                    .build()
                            )
                    )
            );

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, "finalprojectmod");

    // ADD SOUNDS HERE
//    public static final DeferredHolder<SoundEvent, SoundEvent> CORGI_BARK =
//            SOUNDS.register("corgi_bark", () ->
//                    SoundEvent.createVariableRangeEvent(
//                            ResourceLocation.fromNamespaceAndPath("finalprojectmod",
//                                    "corgi_bark")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SPONGEBOB_LAUGH =
            SOUNDS.register("spongebob_laugh", () ->
                    SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("finalprojectmod",
                                    "spongebob_laugh")));

    // Creates a creative tab with the id "finalprojectmod:example_tab" for the example item, that is placed after the combat tab
    // ADD TO CREATIVE TAB HERE
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.finalprojectmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            // .icon(() -> CORGI_DISPENSER_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SPONGEBOB_BLOCK_ITEM.get());
                output.accept(PLANKTON.get());
                output.accept(SPATULA.get());
                output.accept(KRABBY_PATTY.get());
                output.accept(SECRET_FORMULA.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public FinalProjectMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register our interactions handler
        NeoForge.EVENT_BUS.register(PlayerInteractions.class);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register sounds
        SOUNDS.register(modEventBus);

        // Register our custom entities
        // ModEntities.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (FinalProjectMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        // modEventBus.addListener(this::addCreative);

        // Client-only: register renderers
//        if (FMLEnvironment.dist == Dist.CLIENT) {
//            modEventBus.addListener((EntityRenderersEvent.RegisterRenderers e) ->
//                    FinalProjectModClient.onRegisterRenderers(e));
//        }

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            // event.accept(CORGI_DISPENSER_BLOCK_ITEM);
//        }
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
