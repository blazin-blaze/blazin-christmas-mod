package net.blazinblaze.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EnchantedEffectItem extends Item {
    public EnchantedEffectItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
