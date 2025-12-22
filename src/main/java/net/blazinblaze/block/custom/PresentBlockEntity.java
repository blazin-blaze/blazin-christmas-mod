package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlockEntities;
import net.blazinblaze.screen.PresentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class PresentBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> inventory;
    private String gifterName;
    //private boolean openedBefore;
    private DyeColor colorType;

    public PresentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BCMBlockEntities.PRESENT_BLOCK_ENTITY, blockPos, blockState);

        if(blockState.getBlock() instanceof PresentBlock presentBlock) {
            this.colorType = presentBlock.getDyeColor();
        }else {
            this.colorType = DyeColor.LIGHT_GRAY;
        }

        //this.openedBefore = false;
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.gifterName = view.getStringOr("gifterUUID", "test");
        //this.openedBefore = view.getBoolean("openedBefore", false);
        this.readInventoryNbt(view);
        this.colorType = DyeColor.byName(view.getStringOr("colorType", "light_gray"), DyeColor.LIGHT_GRAY);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putString("gifterUUID", this.gifterName != null ? this.gifterName : "test");
        //view.putBoolean("openedBefore", this.openedBefore);
        view.putString("colorType", this.colorType.getName());
        if (!this.trySaveLootTable(view)) {
            ContainerHelper.saveAllItems(view, this.inventory, false);
        }
    }

    public void readInventoryNbt(ValueInput readView) {
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(readView)) {
            ContainerHelper.loadAllItems(readView, this.inventory);
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new PresentHandler(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
    }

    public void setGifterName(String str) {
        this.gifterName = str;
        this.setChanged();
        if(this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(worldPosition, this.getLevel().getBlockState(worldPosition), this.getLevel().getBlockState(worldPosition), PresentBlock.UPDATE_ALL);
        }
    }

    public String getGifterName() {
        return this.gifterName;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
