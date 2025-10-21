package expcontainer.block.entity;

import expcontainer.Experience_Container;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ExperienceContainerBlockEntity extends BlockEntity {
    private short storedXp = 0;

    public ExperienceContainerBlockEntity(BlockPos pos, BlockState state) {
        super(ExperienceContainerBlockEntityType.EXPERIENCE_CONTAINER_BLOCK_ENTITY, pos, state);
    }
    public int addXp(int amount) {
        storedXp += amount;
        markDirty();
        return amount;
    }
    public int getStoredXp() {
        return storedXp;
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("StoredXp", storedXp);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        storedXp = nbt.getShort("StoredXp");
    }
    public static void registerNBT() {
        Experience_Container.LOGGER.info("Registering nbt tags");
    }
}

