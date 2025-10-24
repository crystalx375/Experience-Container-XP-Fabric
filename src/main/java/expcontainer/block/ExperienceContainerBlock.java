package expcontainer.block;

import expcontainer.Experience_Container;
import expcontainer.block.entity.ExperienceContainerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class ExperienceContainerBlock extends BlockWithEntity {

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static DirectionProperty FACING = Properties.FACING;
    final int maxXp = 1628;
    final float factor = 0.9f; // loss 10%



    public ExperienceContainerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(ACTIVE, false)
                .with(FACING, Direction.DOWN));
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getPlayerLookDirection().getOpposite();

        ItemStack stack = ctx.getStack();
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        boolean active = blockEntityTag != null && blockEntityTag.getInt("StoredXp") > 0;

        return this.getDefaultState().with(FACING, facing).with(ACTIVE, active);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExperienceContainerBlockEntity(pos, state);
    }

    private void dropStoredXp(World world, BlockPos pos, int amount) {
        if (amount > 0) {
            Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            ExperienceOrbEntity.spawn((ServerWorld) world, center, amount);
        }
    }
    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        boolean hasSilkTouch = EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0;


        if (world.isClient() || !(blockEntity instanceof ExperienceContainerBlockEntity xpBe)) {
            return;
        }
        int storedXp = xpBe.getStoredXp();
        if (hasSilkTouch) {

            if (storedXp > 0) {
                NbtCompound nbt = new NbtCompound();
                xpBe.writeNbt(nbt);

                nbt.remove("x");
                nbt.remove("y");
                nbt.remove("z");

                ItemStack dropStackItem = new ItemStack(this);
                dropStackItem.setSubNbt("BlockEntityTag", nbt);

                dropStackItem.getOrCreateNbt().putInt("CustomModelData", 1);
                dropStackItem.getOrCreateNbt().putBoolean("Active", true);

                dropStack(world, pos, dropStackItem);
            } else {
                dropStoredXp(world, pos, storedXp);
                ItemStack empty = new ItemStack(this);
                empty.getOrCreateNbt().putInt("CustomModelData", 0);
                dropStack(world, pos, empty);
            }
            if (xpBe.getStoredXp() < 0) {
                int rewrite = xpBe.getStoredXp();
                xpBe.addXp(rewrite * -1);
                Experience_Container.LOGGER.error("Experience barrel exp is negative, so set barrel exp to 0");
            }
        } else {
            if (storedXp >= 0) {

                dropStoredXp(world, pos, storedXp);
                ItemStack Default = new ItemStack(this);
                dropStack(world, pos, Default);
            }
        }
    }
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient) return;

        if (itemStack.hasNbt()) {
            NbtCompound nbt = itemStack.getSubNbt("BlockEntityTag");
            if (nbt != null && nbt.contains("StoredXp")) {
                short storedXp = nbt.getShort("StoredXp");
                boolean active = storedXp > 0;
                world.setBlockState(pos, state.with(ACTIVE, active), Block.NOTIFY_ALL);
                BlockEntity be = world.getBlockEntity(pos);

                if (be instanceof ExperienceContainerBlockEntity xpBe) {
                    xpBe.readNbt(nbt);
                }
            }
        }
    }
    // Levels calculating ahhh
    public static int getExperienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }
    public static int getLevelFromExperience(int xp) {
        int level = 0;
        while (xp >= getExperienceForLevel(level + 1)) {
            level++;
        }
        return level;
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ExperienceContainerBlockEntity xpBe) {
                int newLevel = getLevelFromExperience(xpBe.getStoredXp());

                int current = xpBe.getStoredXp();
                int spaceLeft = maxXp - current;
                int playerXp = player.totalExperience;
                if (spaceLeft < 0) {
                    int rewrite = xpBe.getStoredXp();
                    xpBe.addXp(rewrite * -1);
                    Experience_Container.LOGGER.error("Negative exp in expcontainer?");
                }
                if (spaceLeft == 0) {

                    MutableText part2 = Text.translatable("message.expcontainer.max_level", newLevel)
                            .formatted(Formatting.YELLOW);
                    MutableText finalMessage = Text.translatable("message.expcontainer.full_barrel", part2);

                    world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                            SoundCategory.BLOCKS, 0.6f, 2.0f);
                    player.sendMessage(finalMessage, true);
                    return ActionResult.SUCCESS;
                }
                if (playerXp > 0) {
                    if (spaceLeft > 0) {
                        int playerExp = (int) (playerXp * factor);
                        int added = Math.min(spaceLeft, playerExp);
                        int toRemove = (int) Math.ceil(added / factor);
                        toRemove = Math.min(playerXp, toRemove);
                        int newPlayerXp = playerXp - toRemove;

                        xpBe.addXp(added);

                        player.experienceLevel = 0;
                        player.experienceProgress = 0.0f;
                        player.totalExperience = 0;

                        player.addExperience(newPlayerXp);

                        boolean active = xpBe.getStoredXp() > 0;
                        world.setBlockState(pos, state.with(ACTIVE, active), Block.NOTIFY_ALL);

                        world.playSound(null, pos,
                                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                                SoundCategory.BLOCKS,
                                0.6f,
                                1.0f);
                        int level = getLevelFromExperience(added);
                        MutableText ADDED = Text.translatable("message.expcontainer.added", added, level).formatted(Formatting.WHITE);
                        player.sendMessage(ADDED, true);
                    }
                } else {
                    world.playSound(null, pos,
                            SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                            SoundCategory.BLOCKS,
                            0.3f, 0.5f);
                    MutableText STORED = Text.translatable("message.expcontainer.stored", xpBe.getStoredXp(), newLevel).formatted(Formatting.WHITE);
                    player.sendMessage(STORED, true);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
    public static void registerBlock() {
        Experience_Container.LOGGER.info("Registering blocks to give exp");
    }
}