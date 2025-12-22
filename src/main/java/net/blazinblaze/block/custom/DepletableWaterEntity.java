package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DepletableWaterEntity extends BlockEntity {

    private int tickDeplete;

    public DepletableWaterEntity(BlockPos blockPos, BlockState blockState) {
        super(BCMBlockEntities.DEPLETABLE_WATER_ENTITY, blockPos, blockState);
        this.tickDeplete = 5*20;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, DepletableWaterEntity blockEntity) {
        if(blockEntity.getTickDeplete() <= 0) {
            int x;
            int z;
            int y;
            for(int i = -1; i < 1; i++) {
                for (int i2 = -1; i2 < 1; i2++) {
                    x = (int) pos.getCenter().x();
                    z = (int) pos.getCenter().z();
                    x = x + i;
                    z = z + i2;
                    for (int i3 = -1; i3 < 1; i3++) {
                        y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                        y = y + i3;
                        world.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        blockEntity.setTickDeplete(blockEntity.getTickDeplete() - 1);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        setTickDeplete(view.getInt("tickDeplete").orElseGet(() -> 5*20));
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putInt("tickDeplete", getTickDeplete());
    }

    public int getTickDeplete() {
        return this.tickDeplete;
    }

    public void setTickDeplete(int tickDeplete) {
        this.tickDeplete = tickDeplete;
    }
}
