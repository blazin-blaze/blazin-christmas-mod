package net.blazinblaze.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
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

public class GlassAndPlate extends Block {
    public static final EnumProperty<Direction> FACING;

    public GlassAndPlate(Properties settings) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape original = Stream.of(
                Block.box(1, 1, 1, 10, 2, 10),
                Block.box(2, 0, 2, 9, 1, 9),
                Block.box(11, 0, 11, 14, 3, 14),
                Block.box(11, 1, 10, 14, 3, 11),
                Block.box(11, 1, 14, 14, 3, 15),
                Block.box(10, 1, 11, 11, 3, 14),
                Block.box(14, 1, 11, 15, 3, 14),
                Block.box(11.5, 3, 11.5, 13.5, 7, 13.5)
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
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
