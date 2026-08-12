package crystal.util;

import crystal.block.BlockEntityData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Barrel {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static final int MAX_XP = 1628;

    public static void add(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockEntityData data) {
        if (world.isClient) return;

        final int xpFromPlayer = player.totalExperience;
        final int xpFromBarrel = data.getXp();
        final int levelFromBarrel = getLevelFromExperience(xpFromBarrel);

        final int barrelState = getStateBarrel(xpFromPlayer, xpFromBarrel, MAX_XP);

        if (barrelState == 0) {
            world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.6f, 2.0f);
            player.sendMessage(
                    Text.translatable(
                            "message.experience-container.stored",
                            xpFromBarrel,
                            levelFromBarrel
                    ),
                    true
            );
        }

        if (barrelState == 1) {
            final int left = MAX_XP - xpFromBarrel;
            final int preCheck = (int) Math.ceil(left / 0.95);
            final int remove = Math.min(preCheck, xpFromPlayer);
            final int add = (int) (remove * 0.95);

            player.experienceLevel = 0;
            player.experienceProgress = 0.0f;
            player.totalExperience = 0;

            player.addExperience(xpFromPlayer - remove);
            data.setXp(data.getXp() + add);
            world.setBlockState(pos, state.with(ACTIVE, true), Block.NOTIFY_ALL);

            world.playSound(
                    null,
                    pos,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.BLOCKS,
                    0.6f, 1.0f
            );

            player.sendMessage(
                    Text.translatable(
                            "message.experience-container.added",
                            add,
                            getLevelFromExperience(add)
                    ).formatted(Formatting.WHITE),
                    true
            );
        }

        if (barrelState == 2) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.BLOCKS,
                    0.6f, 2.0f
            );
            player.sendMessage(
                    Text.translatable(
                            "message.experience-container.full_barrel",
                            Text.translatable(
                                    "message.experience-container.max_level",
                                    getLevelFromExperience(MAX_XP)
                            ).formatted(Formatting.YELLOW)
                    ),
                    true
            );
        }
    }

    private static int getStateBarrel(final int xpFromPlayer, final int xpFromBarrel, final int maxXp) {
        if (xpFromPlayer <= 0 && maxXp - xpFromBarrel != 0) return 0;
        else if (maxXp - xpFromBarrel > 0) return 1;
        return 2;
    }

    private static int getExperienceForLevel(final int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }
    public static int getLevelFromExperience(final int xp) {
        int level = 0;
        while (xp >= getExperienceForLevel(level + 1)) {
            level++;
        }
        return level;
    }
}
