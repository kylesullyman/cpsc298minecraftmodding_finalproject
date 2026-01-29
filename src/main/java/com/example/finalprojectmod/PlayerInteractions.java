package com.example.finalprojectmod;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractions {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Level world = event.getLevel();
        InteractionHand hand = event.getHand();

        if (!world.isClientSide) {
            // DO STUFF HERE
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Level world = event.getLevel();
        InteractionHand hand = event.getHand();

        if (!world.isClientSide) {
            // DO STUFF HERE (this is for mobs/entities, not blocks)
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);
        BlockPos pos = event.getPos();

        if (world.isClientSide) return;

        // Must be SpongeBob block no matter what
        if (!world.getBlockState(pos).is(FinalProjectMod.SPONGEBOB_BLOCK.get())) return;

        // --- Spatula / Mega Spatula -> Krabby Patty ---
        if (item.is(FinalProjectMod.SPATULA.get()) || item.is(FinalProjectMod.MEGA_SPATULA.get())) {

            Block.popResource(world, pos.above(), new ItemStack(FinalProjectMod.KRABBY_PATTY.get()));

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        // --- Plankton -> Secret Formula ---
        if (item.is(FinalProjectMod.PLANKTON.get())) {

            // Consume 1 plankton (unless creative)
            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }

            Block.popResource(world, pos.above(), new ItemStack(FinalProjectMod.SECRET_FORMULA.get()));

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity victim = event.getEntity();

        // Only cows
        if (!(victim instanceof Cow)) return;

        // Only player attacks
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        // Only Mega Spatula
        if (!player.getMainHandItem().is(FinalProjectMod.MEGA_SPATULA.get())) return;

        // Mark cow so drops event knows
        victim.getPersistentData().putBoolean("killed_by_mega_spatula", true);

        // Instantly kill
        event.setAmount(99999.0F);
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;

        if (!(event.getEntity() instanceof Cow cow)) return;

        if (!(event.getSource().getEntity() instanceof Player player)) return;

        if (!player.getMainHandItem().is(FinalProjectMod.MEGA_SPATULA.get())) return;

        // Safety check
        if (!cow.getPersistentData().getBoolean("killed_by_mega_spatula").orElse(false)) return;


        // Remove normal drops
        event.getDrops().clear();

        // Drop Krabby Patties
        ItemStack patties = new ItemStack(FinalProjectMod.KRABBY_PATTY.get(), 2);

        ItemEntity drop = new ItemEntity(
                cow.level(),
                cow.getX(), cow.getY(), cow.getZ(),
                patties
        );

        event.getDrops().add(drop);
    }



}
