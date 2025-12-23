package net.blazinblaze.datagen;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.advancement.*;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.item.BCMItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BCMAdvancementProvider extends FabricAdvancementProvider {
    public BCMAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder christmasMod = Advancement.Builder.advancement()
                .display(
                        BCMBlocks.WREATH.asItem(),
                        Component.literal("Blazin' Christmas Mod"),
                        Component.literal("This is the start of a jolly journey."),
                        ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "gui/advancements/backgrounds/bcm_advancements"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("christmas_mod_criterion", CriteriaTriggers.TICK.createCriterion(PlayerTrigger.TriggerInstance.tick().triggerInstance()))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":christmasmod");

        AdvancementHolder santaSpawned = Advancement.Builder.advancement()
                .parent(christmasMod)
                .display(
                        BCMBlocks.SANTA_HEAD.asItem(),
                        Component.literal("Mr. Holly Jolly"),
                        Component.literal("Have Santa spawn in your world."),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("spawned_santa_criterion", BCMCriteria.SPAWNED_SANTA.createCriterion(new SpawnedSantaCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":spawnedsanta");

        AdvancementHolder santaEatCookies = Advancement.Builder.advancement()
                .parent(santaSpawned)
                .display(
                        BCMBlocks.MILK_AND_COOKIES.asItem(),
                        Component.literal("Yum yum!"),
                        Component.literal("Give Santa cookies to eat"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("santa_eat_criterion", BCMCriteria.SANTA_EAT.createCriterion(new SantaEatCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":santaeat");

        AdvancementHolder jollyEssence = Advancement.Builder.advancement()
                .parent(christmasMod)
                .display(
                        BCMItems.JOLLY_ESSENCE,
                        Component.literal("Jolly Epitome"),
                        Component.literal("Collect all 3 festive heads and craft a Jolly Essence"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("jolly_essence_criterion", InventoryChangeTrigger.TriggerInstance.hasItems(BCMItems.JOLLY_ESSENCE))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":jollyessence");

        AdvancementHolder northPole = Advancement.Builder.advancement()
                .parent(jollyEssence)
                .display(
                        BCMItems.SNOWFLAKE,
                        Component.literal("Way Up North!"),
                        Component.literal("Visit the North Pole."),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("north_pole_criterion", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "north_pole"))))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":northpole");

        AdvancementHolder evilSantaSpawned = Advancement.Builder.advancement()
                .parent(northPole)
                .display(
                        BCMBlocks.EVIL_SANTA_SPAWNER,
                        Component.literal("Naughty List"),
                        Component.literal("Spawn Evil Santa"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("spawned_evil_santa_criterion", BCMCriteria.SPAWNED_EVIL_SANTA.createCriterion(new SpawnedEvilSantaCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":evilsantaspawned");

        AdvancementHolder evilSantaKilled = Advancement.Builder.advancement()
                .parent(evilSantaSpawned)
                .display(
                        BCMBlocks.EVIL_SANTA_SPAWNER,
                        Component.literal("Nice List"),
                        Component.literal("Defeat Evil Santa and release the Christmas spirits."),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("killed_evil_santa_criterion", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(wrapperLookup.lookupOrThrow(Registries.ENTITY_TYPE), BCMEntities.EVIL_SANTA_VILLAGER)))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":evilsantakilled");

        AdvancementHolder santaGifted = Advancement.Builder.advancement()
                .parent(santaEatCookies)
                .display(
                        BCMBlocks.RED_PRESENT,
                        Component.literal("Merry Christmas!"),
                        Component.literal("Get a gift from Santa!"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("gifted_santa_criterion", BCMCriteria.GIFTED_SANTA.createCriterion(new GiftedSantaCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":giftedsanta");

        AdvancementHolder milkEggNog = Advancement.Builder.advancement()
                .parent(christmasMod)
                .display(
                        BCMItems.EGG_NOG_BUCKET,
                        Component.literal("Weird Looking Cow..."),
                        Component.literal("Milk an Egg Nog Cow"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("milk_egg_nog_criterion", InventoryChangeTrigger.TriggerInstance.hasItems(BCMItems.EGG_NOG_BUCKET))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":milkeggnog");

        AdvancementHolder snowballLauncher = Advancement.Builder.advancement()
                .parent(christmasMod)
                .display(
                        BCMItems.SNOWBALL_STAFF,
                        Component.literal("Maximum Snowiciency"),
                        Component.literal("Use a Snowball Staff"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("snowball_launcher_criterion", BCMCriteria.SNOWBALL_LAUNCHER.createCriterion(new SnowballLauncherCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":snowballlauncher");

        AdvancementHolder signGift = Advancement.Builder.advancement()
                .parent(christmasMod)
                .display(
                        BCMBlocks.RED_PRESENT,
                        Component.literal("Christmas Consumerism"),
                        Component.literal("Sign a Gift"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("sign_gift_criterion", BCMCriteria.SIGN_GIFT.createCriterion(new SignGiftCriterion.Conditions(Optional.empty())))
                .save(consumer, BlazinChristmasMod.MOD_ID + ":signgift");
    }
}
