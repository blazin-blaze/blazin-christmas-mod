package net.blazinblaze.misc;

import net.blazinblaze.block.BCMBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PresentSlot extends Slot {
    public PresentSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return (stack.is(BCMBlocks.WHITE_PRESENT.asItem()) ||
                stack.is(BCMBlocks.LIGHT_GRAY_PRESENT.asItem()) ||
                stack.is(BCMBlocks.GRAY_PRESENT.asItem()) ||
                stack.is(BCMBlocks.BLACK_PRESENT.asItem()) ||
                stack.is(BCMBlocks.BROWN_PRESENT.asItem()) ||
                stack.is(BCMBlocks.RED_PRESENT.asItem()) ||
                stack.is(BCMBlocks.ORANGE_PRESENT.asItem()) ||
                stack.is(BCMBlocks.YELLOW_PRESENT.asItem()) ||
                stack.is(BCMBlocks.LIME_PRESENT.asItem()) ||
                stack.is(BCMBlocks.GREEN_PRESENT.asItem()) ||
                stack.is(BCMBlocks.CYAN_PRESENT.asItem()) ||
                stack.is(BCMBlocks.LIGHT_BLUE_PRESENT.asItem()) ||
                stack.is(BCMBlocks.BLUE_PRESENT.asItem()) ||
                stack.is(BCMBlocks.PURPLE_PRESENT.asItem()) ||
                stack.is(BCMBlocks.MAGENTA_PRESENT.asItem()) ||
                stack.is(BCMBlocks.PINK_PRESENT.asItem()));
    }
}
