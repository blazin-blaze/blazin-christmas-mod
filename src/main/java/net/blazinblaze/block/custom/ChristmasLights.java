package net.blazinblaze.block.custom;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.Quadrant;
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

import java.util.stream.Stream;

public class ChristmasLights extends Block {

    public static final EnumProperty<Direction> FACING;

    public ChristmasLights(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape original = Stream.of(
                Block.box(0, 14, 14, 16, 16, 16),
                Block.box(13, 11, 14, 15, 14, 16),
                Block.box(13.5, 10.25, 14.5, 14.5, 14, 15.5),
                Block.box(9.5, 10.25, 14.5, 10.5, 14, 15.5),
                Block.box(5.5, 10.25, 14.5, 6.5, 14, 15.5),
                Block.box(1.5, 10.25, 14.5, 2.5, 14, 15.5),
                Block.box(9, 11, 14, 11, 14, 16),
                Block.box(5, 11, 14, 7, 14, 16),
                Block.box(1, 11, 14, 3, 14, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();;

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
