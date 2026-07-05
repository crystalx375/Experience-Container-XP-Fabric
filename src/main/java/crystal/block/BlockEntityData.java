package crystal.block;

import crystal.register.RegisterBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class BlockEntityData extends BlockEntity {

    private int xp;

    public BlockEntityData(BlockPos blockPos, BlockState state) {
        super(RegisterBlockItem.EXPERIENCE_BARRELS, blockPos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("xp", this.xp);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.xp = nbt.getInt("xp");
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.markDirty();
    }
}
