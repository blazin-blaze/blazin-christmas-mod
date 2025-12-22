package net.blazinblaze.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JollyEssence extends Item {
    public JollyEssence(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
