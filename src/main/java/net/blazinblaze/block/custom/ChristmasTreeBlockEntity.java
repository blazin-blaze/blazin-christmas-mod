package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlockEntities;
import net.blazinblaze.screen.ChristmasTreeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ChristmasTreeBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> inventory;

    public ChristmasTreeBlockEntity(BlockPos pos, BlockState state) {
        super(BCMBlockEntities.CHRISTMAS_TREE_BLOCK_ENTITY, pos, state);

        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new ChristmasTreeHandler(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        ContainerHelper.loadAllItems(view, this.inventory);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view, this.inventory);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ChristmasTreeBlockEntity blockEntity) {
        int size = 0;
        for (ItemStack stack : blockEntity.getItems()) {
            if (!stack.isEmpty()) {
                size++;
            }
        }
        switch (size) {
            case 1:
                world.setBlockAndUpdate(pos, state.setValue(ChristmasTreeH1.NUMBER_OF_PRESENTS, 1));
                break;
            case 2:
                world.setBlockAndUpdate(pos, state.setValue(ChristmasTreeH1.NUMBER_OF_PRESENTS, 2));
                break;
            case 3:
                world.setBlockAndUpdate(pos, state.setValue(ChristmasTreeH1.NUMBER_OF_PRESENTS, 3));
                break;
            case 4:
                world.setBlockAndUpdate(pos, state.setValue(ChristmasTreeH1.NUMBER_OF_PRESENTS, 4));
                break;
            default:
                world.setBlockAndUpdate(pos, state.setValue(ChristmasTreeH1.NUMBER_OF_PRESENTS, 0));
        }
    }
}
