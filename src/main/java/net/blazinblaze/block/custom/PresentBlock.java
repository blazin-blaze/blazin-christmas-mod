package net.blazinblaze.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.data.BCMAttachmentTypes;
import net.blazinblaze.data.LastPresentAttachmentData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class PresentBlock extends BaseEntityBlock {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private final DyeColor color;

    private static final VoxelShape SHAPE_CLOSED = Stream.of(
            Block.box(3, 0, 3, 13, 9, 13),
            Block.box(7, 9, 7, 9, 11, 9),
            Block.box(7, 9, 9, 9, 10, 11),
            Block.box(7, 10, 11, 9, 13, 12),
            Block.box(7, 13, 10, 9, 14, 11),
            Block.box(7, 13, 5, 9, 14, 6),
            Block.box(7, 10, 9, 9, 13, 10),
            Block.box(7, 10, 6, 9, 13, 7),
            Block.box(7, 10, 4, 9, 13, 5),
            Block.box(7, 9, 5, 9, 10, 7)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_OPEN = Block.box(3, 0, 3, 13, 9, 13);

    private PresentBlock(Properties settings) {
        this(DyeColor.WHITE, settings);
    }

    public PresentBlock(DyeColor color, Properties settings) {
        super(settings);
        this.color = color;
        this.registerDefaultState((this.stateDefinition.any()).setValue(OPEN, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, PresentBlock::new));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean isOpen = state.getValue(OPEN).booleanValue();
        if(isOpen) {
            return SHAPE_OPEN;
        }else {
            return SHAPE_CLOSED;
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if(world instanceof ServerLevel) {
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof PresentBlockEntity presentBlockEntity && placer instanceof Player player) {
                boolean isNotEmpty = false;
                for(ItemStack stack : presentBlockEntity.getItems()) {
                    if (!stack.isEmpty()) {
                        isNotEmpty = true;
                        break;
                    }
                }
                String str = itemStack.get(BCMComponents.PRESENT_SIGNER);
                if(str != null) {
                    presentBlockEntity.setGifterName(str);
                }
                if(!isNotEmpty && str == null) {
                    player.setAttached(BCMAttachmentTypes.LAST_PRESENT_ATTACHMENT_TYPE, new LastPresentAttachmentData(pos));
                    player.openMenu(presentBlockEntity);
                }
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(player.isShiftKeyDown() && world instanceof ServerLevel) {
            world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof PresentBlockEntity presentBlockEntity) {
                player.setAttached(BCMAttachmentTypes.LAST_PRESENT_ATTACHMENT_TYPE, new LastPresentAttachmentData(pos));
                player.openMenu(presentBlockEntity);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PresentBlockEntity(pos, state);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
            if (!world.isClientSide && player.preventsBlockDrops()) {
                ItemStack itemStack = getBlockFromColor(this.color);
                itemStack.applyComponents(blockEntity.collectComponents());
                itemStack.set(BCMComponents.PRESENT_SIGNER, presentBlockEntity.getGifterName());
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            } else {
                presentBlockEntity.unpackLootTable(player);
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    private ItemStack getBlockFromColor(DyeColor color) {
        return new ItemStack(switch (color) {
            case WHITE -> BCMBlocks.WHITE_PRESENT;
            case ORANGE -> BCMBlocks.ORANGE_PRESENT;
            case MAGENTA -> BCMBlocks.MAGENTA_PRESENT;
            case LIGHT_BLUE -> BCMBlocks.LIGHT_BLUE_PRESENT;
            case YELLOW -> BCMBlocks.YELLOW_PRESENT;
            case LIME -> BCMBlocks.LIME_PRESENT;
            case PINK -> BCMBlocks.PINK_PRESENT;
            case GRAY -> BCMBlocks.GRAY_PRESENT;
            case LIGHT_GRAY -> BCMBlocks.LIGHT_GRAY_PRESENT;
            case CYAN -> BCMBlocks.CYAN_PRESENT;
            case BLUE -> BCMBlocks.BLUE_PRESENT;
            case BROWN -> BCMBlocks.BROWN_PRESENT;
            case GREEN -> BCMBlocks.GREEN_PRESENT;
            case RED -> BCMBlocks.RED_PRESENT;
            case BLACK -> BCMBlocks.BLACK_PRESENT;
            case PURPLE -> BCMBlocks.PURPLE_PRESENT;
        });
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
            builder = builder.withDynamicDrop(ResourceLocation.withDefaultNamespace("contents"),lootConsumer -> {
                for (int i = 0; i < presentBlockEntity.getContainerSize(); i++) {
                    lootConsumer.accept(presentBlockEntity.getItem(i));
                }
            });
        }
        return super.getDrops(state, builder);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }

    public DyeColor getDyeColor() {
        return this.color;
    }
}
