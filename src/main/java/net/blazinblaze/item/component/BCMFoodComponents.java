package net.blazinblaze.item.component;

import net.minecraft.world.food.FoodProperties;

public class BCMFoodComponents {
    public static final FoodProperties COOKIES_PLATE = new FoodProperties.Builder().nutrition(8).saturationModifier(1.0F).build();
    public static final FoodProperties CHRISTMAS_TREAT = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build();

    public static void initialize() {

    }
}
