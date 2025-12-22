package net.blazinblaze.block.custom;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class SnowballPatch extends VineBlock {

    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final IntegerProperty SNOWBALL_POS = IntegerProperty.create("snowball_pos", 0, 3);
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = PipeBlock.PROPERTY_BY_DIRECTION;

    private final Function<BlockState, VoxelShape> shapeFunction;

    public SnowballPatch(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(DOWN, false).setValue(SNOWBALL_POS, 0));
        this.shapeFunction = makeShapes();
    }

    private Function<BlockState, VoxelShape> makeShapes() {
        Map<Direction, VoxelShape> map = Shapes.rotateAll(Block.boxZ(16.0, 0.0, 1.0));
        return this.getShapeForEachState(state -> {
            VoxelShape voxelShape = Shapes.empty();

            for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
                if ((Boolean)state.getValue((Property)entry.getValue())) {
                    voxelShape = Shapes.or(voxelShape, (VoxelShape)map.get(entry.getKey()));
                }
            }

            return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
        });
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.shapeFunction.apply(state);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.hasAdjacentBlocks(this.getUpdatedState(state, world, pos), pos, world);
    }

    private boolean hasAdjacentBlocks(BlockState state, BlockPos pos, BlockGetter world) {
        if(state.getValue(DOWN)) {
            BlockPos below = pos.below();
            return !world.getBlockState(below).is(BlockTags.AIR);
        }
        return this.countFaces(state) > 0;
    }

    private int countFaces(BlockState state) {
        int i = 0;

        for (BooleanProperty booleanProperty : PROPERTY_BY_DIRECTION.values()) {
            if ((Boolean)state.getValue(booleanProperty)) {
                i++;
            }
        }

        return i;
    }

    private boolean canSupportAtFace(BlockGetter world, BlockPos pos, Direction side) {
        BlockPos blockPos = pos.relative(side);
        if (isAcceptableNeighbour(world, blockPos, side)) {
            return true;
        } else if (side.getAxis() == Direction.Axis.Y) {
            return false;
        } else {
            BooleanProperty booleanProperty = (BooleanProperty)PROPERTY_BY_DIRECTION.get(side);
            BlockState blockState = world.getBlockState(pos.above());
            return blockState.is(this) && (Boolean)blockState.getValue(booleanProperty);
        }
    }

    private BlockState getUpdatedState(BlockState state, BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        if ((Boolean)state.getValue(UP)) {
            state = state.setValue(UP, isAcceptableNeighbour(world, blockPos, Direction.DOWN));
        }else if(state.getValue(DOWN)) {
            state = state.setValue(DOWN, true);
        }

        BlockState blockState = null;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty booleanProperty = getPropertyForFace(direction);
            if ((Boolean)state.getValue(booleanProperty)) {
                boolean bl = this.canSupportAtFace(world, pos, direction);
                if (!bl) {
                    if (blockState == null) {
                        blockState = world.getBlockState(blockPos);
                    }

                    bl = blockState.is(this) && (Boolean)blockState.getValue(booleanProperty);
                }

                state = state.setValue(booleanProperty, bl);
            }
        }

        return state;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader world,
            ScheduledTickAccess tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        BlockState blockState = this.getUpdatedState(state, world, pos);
        return !this.hasAdjacentBlocks(blockState, pos, world) ? Blocks.AIR.defaultBlockState() : blockState;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if(random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        return blockState.is(this) ? this.countFaces(blockState) < PROPERTY_BY_DIRECTION.size() : super.canBeReplaced(state, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        boolean bl = blockState.is(this);
        BlockState blockState2 = bl ? blockState : this.defaultBlockState();

        java.util.Random random = new java.util.Random();
        int randomPos = random.nextInt(0, 4);

        for (Direction direction : ctx.getNearestLookingDirections()) {
            BooleanProperty booleanProperty = getPropertyForFace(direction);
            boolean bl2 = bl && (Boolean)blockState.getValue(booleanProperty);
            if (!bl2 && this.canSupportAtFace(ctx.getLevel(), ctx.getClickedPos(), direction)) {
                return blockState2.setValue(booleanProperty, true).setValue(SNOWBALL_POS, randomPos);
            }
        }

        return bl ? blockState2.setValue(SNOWBALL_POS, randomPos) : null;
    }

    public static BooleanProperty getPropertyForFace(Direction direction) {
        return (BooleanProperty)PROPERTY_BY_DIRECTION.get(direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, SNOWBALL_POS);
    }
}
