package com.example.finalprojectmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpongebobBlock extends Block {

    public SpongebobBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // call vanilla (important) and grab the return value
        BlockState result = super.playerWillDestroy(level, pos, state, player);

        if (level.isClientSide) return result;
        ServerLevel serverLevel = (ServerLevel) level;

        boolean usingFishingRod =
                player.getMainHandItem().is(Items.FISHING_ROD) ||
                        player.getOffhandItem().is(Items.FISHING_ROD);

        if (usingFishingRod) {
            // permanent removal: drop the block item once
            popResource(serverLevel, pos, new ItemStack(this.asItem()));
            return result;
        }

        // otherwise: respawn within 5 blocks of the player, no drop
        BlockPos respawnPos = findRespawnPosNearPlayer(serverLevel, player.blockPosition(), 5);

        if (respawnPos != null) {
            serverLevel.sendParticles(
                    ParticleTypes.BUBBLE,
                    pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                    40,
                    0.5, 0.6, 0.5,
                    0.08
            );

            serverLevel.setBlock(respawnPos, this.defaultBlockState(), Block.UPDATE_ALL);

            serverLevel.sendParticles(
                    ParticleTypes.BUBBLE_POP,
                    pos.getX() + 0.5, pos.getY() + 1.05, pos.getZ() + 0.5,
                    15,
                    0.3, 0.3, 0.3,
                    0.0
            );

        } else {
            serverLevel.setBlock(pos, this.defaultBlockState(), Block.UPDATE_ALL);
        }

        return result;
    }

    private BlockPos findRespawnPosNearPlayer(ServerLevel level, BlockPos playerPos, int radius) {
        RandomSource rand = level.getRandom();

        for (int i = 0; i < 40; i++) {
            int dx = rand.nextInt(radius * 2 + 1) - radius; // [-r, r]
            int dz = rand.nextInt(radius * 2 + 1) - radius;
            int dy = rand.nextInt(3) - 1; // -1..+1

            BlockPos candidate = playerPos.offset(dx, dy, dz);

            if (level.isEmptyBlock(candidate)) {
                return candidate;
            }
        }
        return null;
    }
}
