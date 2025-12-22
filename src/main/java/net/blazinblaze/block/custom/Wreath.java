package net.blazinblaze.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import com.mojang.math.OctahedralGroup;
import com.mojang.math.Quadrant;
import java.util.stream.Stream;

public class Wreath extends Block {

    public static final EnumProperty<Direction> FACING;

    public Wreath(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape original = Stream.of(
                Block.box(6, 12, 14, 10, 15, 16),
                Block.box(6, 0, 14, 10, 3, 16),
                Block.box(10, 2, 14, 13, 6, 16),
                Block.box(3, 2, 14, 6, 6, 16),
                Block.box(3, 9, 14, 6, 13, 16),
                Block.box(10, 9, 14, 13, 13, 16),
                Block.box(6, 6, 13, 10.5, 13, 15),
                Block.box(6.5, 11, 13, 7, 12, 15),
                Block.box(13, 5, 14, 16, 10, 16),
                Block.box(0, 5, 14, 3, 10, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        Direction dir = state.getValue(FACING);

        Vec3 anchor = new Vec3(0.5,0.5,0.5);

        if(dir == Direction.EAST) {
            return Shapes.rotate(original, OctahedralGroup.fromXYAngles(Quadrant.R0, Quadrant.R90), anchor);
        }else if(dir == Direction.WEST){
            return Shapes.rotate(original, OctahedralGroup.fromXYAngles(Quadrant.R0, Quadrant.R270), anchor);
        }else if(dir == Direction.SOUTH){
            return Shapes.rotate(original, OctahedralGroup.fromXYAngles(Quadrant.R0, Quadrant.R180), anchor);
        }

        return original;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
