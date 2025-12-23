package net.blazinblaze.misc;

import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.entity.custom.*;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBCMProvider {

    private static final List<String> possibleSnowballTypes = List.of(
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane",
            "fiery",
            "withered",
            "ethereal",
            "candy_cane"
    );

    private static final List<Block> possibleFestiveHeads = List.of(BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD,
            BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD,
            BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD,
            BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD,
            BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD,
            BCMBlocks.SNOWMAN_HEAD, BCMBlocks.SANTA_HEAD, BCMBlocks.GINGERBREAD_HEAD);

    private static final List<Holder<Potion>> possiblePotions = List.of(Potions.HARMING, Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE, Potions.HEALING, Potions.INFESTED, Potions.INVISIBILITY, Potions.LEAPING, Potions.LONG_LEAPING,
            Potions.LONG_INVISIBILITY, Potions.WIND_CHARGED, Potions.WEAVING, Potions.WEAKNESS, Potions.WATER_BREATHING, Potions.TURTLE_MASTER, Potions.SWIFTNESS, Potions.STRONG_TURTLE_MASTER, Potions.STRONG_SWIFTNESS, Potions.STRONG_STRENGTH,
            Potions.STRONG_SLOWNESS, Potions.STRONG_REGENERATION, Potions.STRONG_POISON, Potions.STRONG_LEAPING, Potions.STRONG_HEALING, Potions.STRONG_HARMING, Potions.STRENGTH, Potions.SLOWNESS, Potions.SLOW_FALLING, Potions.REGENERATION,
            Potions.POISON, Potions.OOZING, Potions.NIGHT_VISION, Potions.LUCK, Potions.LONG_WEAKNESS, Potions.LONG_WATER_BREATHING, Potions.LONG_TURTLE_MASTER, Potions.LONG_SWIFTNESS, Potions.LONG_STRENGTH, Potions.LONG_SLOWNESS, Potions.LONG_SLOW_FALLING,
            Potions.LONG_REGENERATION, Potions.LONG_POISON, Potions.LONG_NIGHT_VISION);

    private static final List<DyeColor> possibleColors = List.of(
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.BLUE,
            DyeColor.LIGHT_BLUE,
            DyeColor.PINK,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.CYAN,
            DyeColor.BROWN
    );

    private static String getRandomKey(Random random) {
        int rndmInt = random.nextInt(0, possibleSnowballTypes.size());
        return possibleSnowballTypes.get(rndmInt);
    }

    public static void createRandomSnowball(LivingEntity entity, ServerLevel serverLevel, double d, double e, double g, double h, float power, boolean allowWaterSnow) {
        float limitedPower = Math.min(power, 1.5F);
        String key = getRandomKey(new Random());

        if(key.matches("fiery")) {
            ItemStack itemStack = new ItemStack(BCMItems.FIERY_SNOWBALL);
            Projectile.spawnProjectile(
                    new FierySnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(d, e + h - snowball.getY(), g, limitedPower, 5.0F)
            );
        }else if(key.matches("withered")) {
            ItemStack itemStack = new ItemStack(BCMItems.WITHERED_SNOWBALL);
            Projectile.spawnProjectile(
                    new WitheredSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(d, e + h - snowball.getY(), g, limitedPower, 5.0F)
            );
        }else if(key.matches("candy_cane")) {
            ItemStack itemStack = new ItemStack(BCMItems.CANDY_CANE_SNOWBALL);
            Projectile.spawnProjectile(
                    new CandyCaneSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(d, e + h - snowball.getY(), g, limitedPower, 5.0F)
            );
        }else if(key.matches("ethereal")) {
            if(allowWaterSnow) {
                ItemStack itemStack = new ItemStack(BCMItems.ETHEREAL_SNOWBALL);
                Projectile.spawnProjectile(
                        new EtherealSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(d, e + h - snowball.getY(), g, limitedPower, 5.0F)
                );
            }
        }
    }

    public static void createRandomSnowballSpiral(LivingEntity entity, ServerLevel serverLevel, double d, double e, double g, double h, boolean allowWaterSnow) {
        String key = getRandomKey(new Random());

        for(int i = 0; i < 30; i++) {
            int delta = i*5;
            if(key.matches("fiery")) {
                ItemStack itemStack = new ItemStack(BCMItems.FIERY_SNOWBALL);
                Projectile.spawnProjectile(
                        new FierySnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(Mth.sin((float)d - delta), e + h - snowball.getY(), Mth.sin((float)g - delta), 1.5F, 12.0F)
                );
            }else if(key.matches("withered")) {
                ItemStack itemStack = new ItemStack(BCMItems.CANDY_CANE_SNOWBALL);
                Projectile.spawnProjectile(
                        new CandyCaneSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(Mth.sin((float)d - delta), e + h - snowball.getY(), Mth.sin((float)g - delta), 1.5F, 12.0F)
                );
            }else if(key.matches("candy_cane")) {
                ItemStack itemStack = new ItemStack(BCMItems.CANDY_CANE_SNOWBALL);
                Projectile.spawnProjectile(
                        new CandyCaneSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(Mth.sin((float)d - delta), e + h - snowball.getY(), Mth.sin((float)g - delta), 1.5F, 12.0F)
                );
            }else if(key.matches("ethereal")) {
                if(allowWaterSnow) {
                    ItemStack itemStack = new ItemStack(BCMItems.ETHEREAL_SNOWBALL);
                    Projectile.spawnProjectile(
                            new EtherealSnowballEntity(serverLevel, entity, itemStack), serverLevel, itemStack, snowball -> snowball.shoot(Mth.sin((float)d - delta), e + h - snowball.getY(), Mth.sin((float)g - delta), 1.5F, 12.0F)
                    );
                }
            }
        }
    }

    public static ItemStack getRandomPresentWithoutLoot() {
        Random random = new Random();
        DyeColor color = possibleColors.get(random.nextInt(0, possibleColors.size()));
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

    private static final Map<ItemStack, ItemRarity> LOOT_TABLE = Map.<ItemStack, ItemRarity>ofEntries(
            Map.entry(Items.COAL_BLOCK.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.DIAMOND_SWORD.getDefaultInstance(), new ItemRarity(0.1, false, true, "tool")),
            Map.entry(Items.ANVIL.getDefaultInstance(), new ItemRarity(0.4, false, false, "none")),
            Map.entry(Items.BEACON.getDefaultInstance(), new ItemRarity(0.1, false, false, "none")),
            Map.entry(Items.DIAMOND_BLOCK.getDefaultInstance(), new ItemRarity(0.1, true, false, "none")),
            Map.entry(Items.NETHERITE_BLOCK.getDefaultInstance(), new ItemRarity(0.05, false, false, "none")),
            Map.entry(Items.GOLD_BLOCK.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.IRON_BLOCK.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.LAPIS_BLOCK.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.COPPER_BLOCK.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), new ItemRarity(0.1, true, false, "none")),
            Map.entry(Items.GOLDEN_APPLE.getDefaultInstance(), new ItemRarity(0.2, true, false, "none")),
            Map.entry(Items.SHULKER_BOX.getDefaultInstance(), new ItemRarity(0.1, false, false, "none")),
            Map.entry(Items.ELYTRA.getDefaultInstance(), new ItemRarity(0.01, false, true, "armor")),
            Map.entry(Items.BREAD.getDefaultInstance(), new ItemRarity(0.6, true, false, "none")),
            Map.entry(Items.COOKED_BEEF.getDefaultInstance(), new ItemRarity(0.6, true, false, "none")),
            Map.entry(Items.FIRE_CHARGE.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.FLINT_AND_STEEL.getDefaultInstance(), new ItemRarity(0.5, false, false, "none")),
            Map.entry(Items.OBSIDIAN.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.ENDER_CHEST.getDefaultInstance(), new ItemRarity(0.4, false, false, "none")),
            Map.entry(Items.ENDER_PEARL.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.WOLF_ARMOR.getDefaultInstance(), new ItemRarity(0.4, false, true, "armor")),
            Map.entry(Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), new ItemRarity(0.2, false, true, "armor")),
            Map.entry(Items.GOLDEN_HORSE_ARMOR.getDefaultInstance(), new ItemRarity(0.5, false, true, "armor")),
            Map.entry(Items.TOTEM_OF_UNDYING.getDefaultInstance(), new ItemRarity(0.4, false, false, "none")),
            Map.entry(Items.END_CRYSTAL.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.EMERALD_BLOCK.getDefaultInstance(), new ItemRarity(0.3, true, false, "none")),
            Map.entry(Items.DIAMOND_PICKAXE.getDefaultInstance(), new ItemRarity(0.1, false, true, "tool")),
            Map.entry(Items.DIAMOND_AXE.getDefaultInstance(), new ItemRarity(0.1, false, true, "tool")),
            Map.entry(Items.DIAMOND_SHOVEL.getDefaultInstance(), new ItemRarity(0.1, false, true, "tool")),
            Map.entry(Items.DIAMOND_HOE.getDefaultInstance(), new ItemRarity(0.1, false, true, "tool")),
            Map.entry(Items.IRON_SWORD.getDefaultInstance(), new ItemRarity(0.3, false, true, "tool")),
            Map.entry(Items.IRON_PICKAXE.getDefaultInstance(), new ItemRarity(0.3, false, true, "tool")),
            Map.entry(Items.IRON_AXE.getDefaultInstance(), new ItemRarity(0.3, false, true, "tool")),
            Map.entry(Items.IRON_SHOVEL.getDefaultInstance(), new ItemRarity(0.3, false, true, "tool")),
            Map.entry(Items.IRON_HOE.getDefaultInstance(), new ItemRarity(0.3, false, true, "tool")),
            Map.entry(Items.NETHERITE_AXE.getDefaultInstance(), new ItemRarity(0.05, false, true, "tool")),
            Map.entry(Items.NETHERITE_HOE.getDefaultInstance(), new ItemRarity(0.05, false, true, "tool")),
            Map.entry(Items.NETHERITE_SWORD.getDefaultInstance(), new ItemRarity(0.05, false, true, "tool")),
            Map.entry(Items.NETHERITE_PICKAXE.getDefaultInstance(), new ItemRarity(0.05, false, true, "tool")),
            Map.entry(Items.NETHERITE_SHOVEL.getDefaultInstance(), new ItemRarity(0.05, false, true, "tool")),
            Map.entry(Items.IRON_HELMET.getDefaultInstance(), new ItemRarity(0.3, false, true, "armor")),
            Map.entry(Items.IRON_CHESTPLATE.getDefaultInstance(), new ItemRarity(0.3, false, true, "armor")),
            Map.entry(Items.IRON_LEGGINGS.getDefaultInstance(), new ItemRarity(0.3, false, true, "armor")),
            Map.entry(Items.IRON_BOOTS.getDefaultInstance(), new ItemRarity(0.3, false, true, "armor")),
            Map.entry(Items.DIAMOND_HELMET.getDefaultInstance(), new ItemRarity(0.1, false, true, "armor")),
            Map.entry(Items.DIAMOND_CHESTPLATE.getDefaultInstance(), new ItemRarity(0.1, false, true, "armor")),
            Map.entry(Items.DIAMOND_LEGGINGS.getDefaultInstance(), new ItemRarity(0.1, false, true, "armor")),
            Map.entry(Items.DIAMOND_BOOTS.getDefaultInstance(), new ItemRarity(0.1, false, true, "armor")),
            Map.entry(Items.NETHERITE_HELMET.getDefaultInstance(), new ItemRarity(0.05, false, true, "armor")),
            Map.entry(Items.NETHERITE_CHESTPLATE.getDefaultInstance(), new ItemRarity(0.05, false, true, "armor")),
            Map.entry(Items.NETHERITE_LEGGINGS.getDefaultInstance(), new ItemRarity(0.05, false, true, "armor")),
            Map.entry(Items.NETHERITE_BOOTS.getDefaultInstance(), new ItemRarity(0.05, false, true, "armor")),
            Map.entry(Items.POTION.getDefaultInstance(), new ItemRarity(0.5, false, false, "none")),
            Map.entry(Items.TNT.getDefaultInstance(), new ItemRarity(0.5, true, false, "none")),
            Map.entry(Items.BOW.getDefaultInstance(), new ItemRarity(0.5, false, true, "bow")),
            Map.entry(Items.DIAMOND.getDefaultInstance(), new ItemRarity(0.5, true, true, "crossbow")),
            Map.entry(Items.IRON_INGOT.getDefaultInstance(), new ItemRarity(0.5, true, true, "crossbow")),
            Map.entry(Items.GOLD_INGOT.getDefaultInstance(), new ItemRarity(0.5, true, true, "crossbow")),
            Map.entry(Items.EMERALD.getDefaultInstance(), new ItemRarity(0.5, true, true, "crossbow")),
            Map.entry(Items.NETHERITE_INGOT.getDefaultInstance(), new ItemRarity(0.09, true, true, "crossbow")),
            Map.entry(Items.COAL.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMItems.FIERY_SNOWBALL.getDefaultInstance(), new ItemRarity(0.5, true, true, "crossbow")),
            Map.entry(BCMItems.WITHERED_SNOWBALL.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMItems.ETHEREAL_SNOWBALL.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMItems.CANDY_CANE_SNOWBALL.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMItems.CANDY_CANE.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMItems.GINGERBREAD.getDefaultInstance(), new ItemRarity(0.8, true, true, "crossbow")),
            Map.entry(BCMBlocks.GLASS_OF_EGG_NOG.asItem().getDefaultInstance(), new ItemRarity(0.8, false, true, "crossbow")),
            Map.entry(BCMBlocks.GLASS_OF_MILK.asItem().getDefaultInstance(), new ItemRarity(0.8, false, true, "crossbow")));

    public static ItemStack getRandomPresent() {
        Random random = new Random();
        DyeColor color = possibleColors.get(random.nextInt(0, possibleColors.size()));
        ItemStack temp = new ItemStack(switch (color) {
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

        int currentCount = random.nextInt(3,9);
        int numberOfItems = 0;

        ArrayList<ItemStack> addedItems = new ArrayList<>();

        do {
            List<ItemStack> list = LOOT_TABLE.keySet().stream().toList();
            ItemStack itemReference = list.get(random.nextInt(list.size()));
            ItemStack item = itemReference.copy();
            ItemRarity rarity = LOOT_TABLE.get(itemReference);
            double threshold = rarity.threshold();

            if (ThreadLocalRandom.current().nextDouble() < threshold) {
                boolean multiple = rarity.multiple();

                if (multiple) {
                    if (ThreadLocalRandom.current().nextDouble() < 0.5) {
                        item.setCount(random.nextInt(16));
                        addedItems.add(item);
                        numberOfItems++;
                    } else {
                        addedItems.add(item);
                        numberOfItems++;
                    }
                } else {
                    Item stackItem = item.getItem();
                    if (stackItem == Items.POTION) {
                        ItemStack potionItem = PotionContents.createItemStack(Items.POTION, possiblePotions.get(random.nextInt(possiblePotions.size())));
                        addedItems.add(potionItem);
                        numberOfItems++;
                    } else {
                        addedItems.add(item);
                        numberOfItems++;
                    }
                }
            }else {
                addedItems.add(ItemStack.EMPTY);
                numberOfItems++;
            }
        }while(numberOfItems < currentCount);

        if(addedItems.size() > 9) {
            addedItems.subList(9, addedItems.size()).clear();
        }

        temp.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(addedItems));
        temp.set(BCMComponents.PRESENT_SIGNER, "Santa");
        return temp;
    }

    public static Block getRandomFestiveHead() {
        Random random = new Random();
        return possibleFestiveHeads.get(random.nextInt(0, possibleFestiveHeads.size()));
    }

    record ItemRarity(double threshold, boolean multiple, boolean isEnchantable, String enchantableType) {}
}
