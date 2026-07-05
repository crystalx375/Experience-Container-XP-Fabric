package crystal.block;

import crystal.util.Barrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static crystal.util.Barrel.ACTIVE;
import static net.minecraft.block.BarrelBlock.FACING;

public class ExperienceContainerBlock extends Block implements BlockEntityProvider {

    public ExperienceContainerBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(ACTIVE, false)
                .with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerLookDirection().getOpposite();
        return this.getDefaultState().with(FACING, facing);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityData(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            final BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityData data) {
                Barrel.add(state, world, pos, player, data);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (world.isClient() || !(blockEntity instanceof BlockEntityData data)) {
            return;
        }

        final var silk = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        final int level = EnchantmentHelper.getLevel(silk, tool);

        if (level > 0) {
            final NbtCompound nbt = new NbtCompound();
            final ItemStack stack = new ItemStack(this);
            nbt.putInt("xp", data.getXp());
            nbt.putBoolean("active", data.getXp() > 0);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
            dropStack(world, pos, stack);
        } else {
            ItemStack empty = new ItemStack(this);
            empty.remove(DataComponentTypes.CUSTOM_DATA);
            dropStack(world, pos, empty);
            dropStoredXp(world, pos, data.getXp());
        }
    }

    private void dropStoredXp(World world, BlockPos pos, final int amount) {
        if (amount > 0) {
            final Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            ExperienceOrbEntity.spawn((ServerWorld) world, center, amount);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient) {
            final BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof BlockEntityData data) {
                final NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
                if (nbtComponent != null) {
                    final NbtCompound nbt = nbtComponent.copyNbt();
                    final int xpFromBarrel = nbt.getInt("xp");
                    data.setXp(xpFromBarrel);
                    world.setBlockState(pos, state.with(ACTIVE, xpFromBarrel > 0), Block.NOTIFY_ALL);
                    world.markDirty(pos);
                }
            }
        }
    }
}
