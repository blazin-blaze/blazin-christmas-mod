package net.blazinblaze.item;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.item.component.BCMFoodComponents;
import net.blazinblaze.item.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumables;
import java.util.function.Function;

public class BCMItems {

    public static final Item EGG_NOG_BUCKET = register("egg_nog_bucket", Item::new, new Item.Properties().craftRemainder(Items.BUCKET).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).usingConvertsTo(Items.BUCKET).stacksTo(1));
    public static final Item CANDY_CANE = register("candy_cane", Item::new, new Item.Properties().food(BCMFoodComponents.CHRISTMAS_TREAT, Consumables.DEFAULT_FOOD));
    public static final Item GINGERBREAD = register("gingerbread", Item::new, new Item.Properties().food(BCMFoodComponents.CHRISTMAS_TREAT, Consumables.DEFAULT_FOOD));
    public static final Item JOLLY_ESSENCE = register("jolly_essence", JollyEssence::new, new Item.Properties().rarity(Rarity.RARE));
    public static final Item BLUE_WHITE_ORNAMENT = register("blue_white_ornament", Item::new, new Item.Properties().stacksTo(1));
    public static final Item RED_GREEN_ORNAMENT = register("red_green_ornament", Item::new, new Item.Properties().stacksTo(1));
    public static final Item SILVER_GOLD_ORNAMENT = register("silver_gold_ornament", Item::new, new Item.Properties().stacksTo(1));
    public static final Item WITHERED_SNOWBALL = register("withered_snowball", WitheredSnowball::new, new Item.Properties().stacksTo(16));
    public static final Item FIERY_SNOWBALL = register("fiery_snowball", FierySnowball::new, new Item.Properties().stacksTo(16));
    public static final Item ETHEREAL_SNOWBALL = register("ethereal_snowball", EtherealSnowball::new, new Item.Properties().stacksTo(16));
    public static final Item CANDY_CANE_SNOWBALL = register("candy_cane_snowball", CandyCaneSnowball::new, new Item.Properties().stacksTo(16));
    public static final Item SNOWBALL_STAFF = register("snowball_staff", SnowballStaff::new, new Item.Properties().stacksTo(1).component(BCMComponents.IS_SNOWBALL_LOADED, false).component(BCMComponents.SNOWBALL_STAFF_TYPE, "normal"));

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
    }
}
