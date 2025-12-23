package net.blazinblaze.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blazinblaze.block.BCMBlockEntities;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ChristmasTreeH1 extends BaseEntityBlock {

    public static final IntegerProperty NUMBER_OF_PRESENTS = IntegerProperty.create("number_of_presents", 0, 4);

    private int lastInventoryUpdate = 0;

    private static final VoxelShape shapeEmpty = Stream.of(
            Block.box(6, 0.5, 6, 10, 27, 10),
            Block.box(6, 28, 6, 10, 30, 10),
            Block.box(1, 17, 1, 15, 19, 15),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 20, 2, 14, 22, 14),
            Block.box(4, 23, 4, 12, 25, 12),
            Block.box(5, 26, 5, 11, 28, 11),
            Block.box(0, 0, 0, 16, 0.6, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape shapeP1 = Stream.of(
            Block.box(6, 0.5, 6, 10, 27, 10),
            Block.box(6, 28, 6, 10, 30, 10),
            Block.box(1, 17, 1, 15, 19, 15),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 20, 2, 14, 22, 14),
            Block.box(4, 23, 4, 12, 25, 12),
            Block.box(5, 26, 5, 11, 28, 11),
            Block.box(0, 0, 0, 16, 0.6, 16),
            Block.box(9, 0.5, 10, 13, 4.5, 14),
            Block.box(10, 4.5, 11, 12, 5.5, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape shapeP2 = Stream.of(
            Block.box(6, 0.5, 6, 10, 27, 10),
            Block.box(6, 28, 6, 10, 30, 10),
            Block.box(1, 17, 1, 15, 19, 15),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 20, 2, 14, 22, 14),
            Block.box(4, 23, 4, 12, 25, 12),
            Block.box(5, 26, 5, 11, 28, 11),
            Block.box(0, 0, 0, 16, 0.6, 16),
            Block.box(9, 0.5, 10, 13, 4.5, 14),
            Block.box(10, 4.5, 11, 12, 5.5, 13),
            Block.box(1, 0.5, 1, 5, 4.5, 5),
            Block.box(2, 4.5, 2, 4, 5.5, 4)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape shapeP3 = Stream.of(
            Block.box(6, 0.5, 6, 10, 27, 10),
            Block.box(6, 28, 6, 10, 30, 10),
            Block.box(1, 17, 1, 15, 19, 15),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 20, 2, 14, 22, 14),
            Block.box(4, 23, 4, 12, 25, 12),
            Block.box(5, 26, 5, 11, 28, 11),
            Block.box(0, 0, 0, 16, 0.6, 16),
            Block.box(9, 0.5, 10, 13, 4.5, 14),
            Block.box(10, 4.5, 11, 12, 5.5, 13),
            Block.box(1, 0.5, 1, 5, 4.5, 5),
            Block.box(2, 4.5, 2, 4, 5.5, 4),
            Block.box(2, 0.5, 9, 6, 4.5, 13),
            Block.box(3, 4.5, 10, 5, 5.5, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape shapeP4 = Stream.of(
            Block.box(6, 0.5, 6, 10, 27, 10),
            Block.box(6, 28, 6, 10, 30, 10),
            Block.box(1, 17, 1, 15, 19, 15),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 20, 2, 14, 22, 14),
            Block.box(4, 23, 4, 12, 25, 12),
            Block.box(5, 26, 5, 11, 28, 11),
            Block.box(0, 0, 0, 16, 0.6, 16),
            Block.box(9, 0.5, 10, 13, 4.5, 14),
            Block.box(10, 4.5, 11, 12, 5.5, 13),
            Block.box(1, 0.5, 1, 5, 4.5, 5),
            Block.box(2, 4.5, 2, 4, 5.5, 4),
            Block.box(2, 0.5, 9, 6, 4.5, 13),
            Block.box(3, 4.5, 10, 5, 5.5, 12),
            Block.box(10, 0.5, 2, 14, 4.5, 6),
            Block.box(11, 4.5, 3, 13, 5.5, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ChristmasTreeH1(Properties settings) {
        super(settings);
        this.registerDefaultState((this.stateDefinition.any()).setValue(NUMBER_OF_PRESENTS, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, ChristmasTreeH1::new));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int numOfPresents = state.getValue(NUMBER_OF_PRESENTS).intValue();
        if(numOfPresents == 0) {
            return shapeEmpty;
        }else if(numOfPresents == 1) {
            return shapeP1;
        }else if(numOfPresents == 2) {
            return shapeP2;
        }else if(numOfPresents == 3) {
            return shapeP3;
        }else if(numOfPresents == 4) {
            return shapeP4;
        }
        return super.getShape(state, world, pos, context);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 32, 16);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(stack.is(BCMItems.BLUE_WHITE_ORNAMENT) || stack.is(BCMItems.RED_GREEN_ORNAMENT) || stack.is(BCMItems.SILVER_GOLD_ORNAMENT)) {
            BlockState stateUp = world.getBlockState(pos.above());
            if(stateUp.is(BCMBlocks.CHRISTMAS_TREE_H2)) {
                boolean greenRed = stateUp.getValue(ChristmasTreeH2.GREEN_AND_RED).booleanValue();
                boolean whiteBlue = stateUp.getValue(ChristmasTreeH2.WHITE_AND_BLUE).booleanValue();
                boolean silverGold = stateUp.getValue(ChristmasTreeH2.SILVER_AND_GOLD).booleanValue();
                if(greenRed) {
                    player.addItem(BCMItems.RED_GREEN_ORNAMENT.getDefaultInstance().copy());
                }else if(whiteBlue) {
                    player.addItem(BCMItems.BLUE_WHITE_ORNAMENT.getDefaultInstance().copy());
                }else if(silverGold) {
                    player.addItem(BCMItems.SILVER_GOLD_ORNAMENT.getDefaultInstance().copy());
                }
                if(stack.is(BCMItems.BLUE_WHITE_ORNAMENT)) {
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, true).setValue(ChristmasTreeH2.GREEN_AND_RED, false).setValue(ChristmasTreeH2.SILVER_AND_GOLD, false));
                    stack.consume(1, player);
                    return InteractionResult.SUCCESS;
                }else if(stack.is(BCMItems.RED_GREEN_ORNAMENT)) {
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, false).setValue(ChristmasTreeH2.GREEN_AND_RED, true).setValue(ChristmasTreeH2.SILVER_AND_GOLD, false));
                    stack.consume(1, player);
                    return InteractionResult.SUCCESS;
                }else if(stack.is(BCMItems.SILVER_GOLD_ORNAMENT)) {
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, false).setValue(ChristmasTreeH2.GREEN_AND_RED, false).setValue(ChristmasTreeH2.SILVER_AND_GOLD, true));
                    stack.consume(1, player);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(player.isShiftKeyDown()) {
            BlockState stateUp = world.getBlockState(pos.above());
            if(stateUp.is(BCMBlocks.CHRISTMAS_TREE_H2)) {
                boolean greenRed = stateUp.getValue(ChristmasTreeH2.GREEN_AND_RED).booleanValue();
                boolean whiteBlue = stateUp.getValue(ChristmasTreeH2.WHITE_AND_BLUE).booleanValue();
                boolean silverGold = stateUp.getValue(ChristmasTreeH2.SILVER_AND_GOLD).booleanValue();
                if(greenRed) {
                    player.addItem(BCMItems.RED_GREEN_ORNAMENT.getDefaultInstance().copy());
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, false).setValue(ChristmasTreeH2.GREEN_AND_RED, false).setValue(ChristmasTreeH2.SILVER_AND_GOLD, false));
                    return InteractionResult.SUCCESS;
                }else if(whiteBlue) {
                    player.addItem(BCMItems.BLUE_WHITE_ORNAMENT.getDefaultInstance().copy());
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, false).setValue(ChristmasTreeH2.GREEN_AND_RED, false).setValue(ChristmasTreeH2.SILVER_AND_GOLD, false));
                    return InteractionResult.SUCCESS;
                }else if(silverGold) {
                    player.addItem(BCMItems.SILVER_GOLD_ORNAMENT.getDefaultInstance().copy());
                    world.setBlockAndUpdate(pos.above(), stateUp.setValue(ChristmasTreeH2.WHITE_AND_BLUE, false).setValue(ChristmasTreeH2.GREEN_AND_RED, false).setValue(ChristmasTreeH2.SILVER_AND_GOLD, false));
                    return InteractionResult.SUCCESS;
                }
                player.playNotifySound(SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
        if (world instanceof ServerLevel) {
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof ChristmasTreeBlockEntity christmasTreeBlockEntity) {
                player.openMenu(christmasTreeBlockEntity);
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockState stateUp = world.getBlockState(pos.above());
        return stateUp.is(BCMBlocks.CHRISTMAS_TREE_H2) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState stateUp = world.getBlockState(pos.above());
        return stateUp.is(BlockTags.AIR);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        BlockState stateUp = world.getBlockState(pos.above());
        if(stateUp.is(BlockTags.AIR) && world instanceof ServerLevel serverWorld) {
            serverWorld.setBlockAndUpdate(pos.above(), BCMBlocks.CHRISTMAS_TREE_H2.defaultBlockState());
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof ChristmasTreeBlockEntity christmasTreeBlockEntity) {
            NonNullList<ItemStack> list = christmasTreeBlockEntity.getItems();
            int size = 0;
            for(ItemStack stack : list) {
                if(!stack.isEmpty()) {
                    size++;
                }
            }

            if(this.lastInventoryUpdate != size) {
                if(size == 0) {
                    world.setBlockAndUpdate(pos, state.setValue(NUMBER_OF_PRESENTS, 0));
                }else if(size == 1) {
                    world.setBlockAndUpdate(pos, state.setValue(NUMBER_OF_PRESENTS, 1));
                }else if(size == 2) {
                    world.setBlockAndUpdate(pos, state.setValue(NUMBER_OF_PRESENTS, 2));
                }else if(size == 3) {
                    world.setBlockAndUpdate(pos, state.setValue(NUMBER_OF_PRESENTS, 3));
                }else if(size == 4) {
                    world.setBlockAndUpdate(pos, state.setValue(NUMBER_OF_PRESENTS, 4));
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NUMBER_OF_PRESENTS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChristmasTreeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : createTickerHelper(type, BCMBlockEntities.CHRISTMAS_TREE_BLOCK_ENTITY, ChristmasTreeBlockEntity::tick);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }
}
