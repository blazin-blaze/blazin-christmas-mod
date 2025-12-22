package net.blazinblaze.block.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blazinblaze.block.BCMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FluidBlockDepletable extends BaseEntityBlock implements BucketPickup {

    public static final IntegerProperty LEVEL;
    protected final FlowingFluid fluid;
    private final List<FluidState> stateCache;
    public static final VoxelShape SHAPE_STABLE;
    public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS;

    public FluidBlockDepletable(FlowingFluid flowingFluid, Properties properties) {
        super(properties);
        this.fluid = flowingFluid;
        this.stateCache = Lists.newArrayList();
        this.stateCache.add(flowingFluid.getSource(false));

        for(int i = 1; i < 8; ++i) {
            this.stateCache.add(flowingFluid.getFlowing(8 - i, false));
        }

        this.stateCache.add(flowingFluid.getFlowing(8, true));
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LEVEL, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return this.fluid.getPickupSound();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DepletableWaterEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : createTickerHelper(type, BCMBlockEntities.DEPLETABLE_WATER_ENTITY, DepletableWaterEntity::tick);
    }

    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isAbove(SHAPE_STABLE, blockPos, true) && (Integer)blockState.getValue(LEVEL) == 0 && collisionContext.canStandOnFluid(blockGetter.getFluidState(blockPos.above()), blockState.getFluidState()) ? SHAPE_STABLE : Shapes.empty();
    }

    protected boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getFluidState().isRandomlyTicking();
    }

    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        blockState.getFluidState().randomTick(serverLevel, blockPos, randomSource);
    }

    protected boolean propagatesSkylightDown(BlockState blockState) {
        return false;
    }

    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return true;
    }

    protected FluidState getFluidState(BlockState blockState) {
        int i = (Integer)blockState.getValue(LEVEL);
        return (FluidState)this.stateCache.get(Math.min(i, 8));
    }

    protected boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
        return blockState2.getFluidState().getType().isSame(this.fluid);
    }

    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    protected List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        return Collections.emptyList();
    }

    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (this.shouldSpreadLiquid(level, blockPos, blockState)) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(level));
        }

    }

    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if (blockState.getFluidState().isSource() || blockState2.getFluidState().isSource()) {
            scheduledTickAccess.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(levelReader));
        }

        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        if (this.shouldSpreadLiquid(level, blockPos, blockState)) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(level));
        }

    }

    private boolean shouldSpreadLiquid(Level level, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    private void fizz(LevelAccessor levelAccessor, BlockPos blockPos) {
        levelAccessor.levelEvent(1501, blockPos, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LEVEL});
    }

    public ItemStack pickupBlock(@Nullable LivingEntity livingEntity, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if ((Integer)blockState.getValue(LEVEL) == 0) {
            levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(this.fluid.getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }

    static {
        LEVEL = BlockStateProperties.LEVEL;
        SHAPE_STABLE = Block.column((double)16.0F, (double)0.0F, (double)8.0F);
        POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);
    }
}
