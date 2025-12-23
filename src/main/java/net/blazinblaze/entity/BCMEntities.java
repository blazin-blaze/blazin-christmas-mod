package net.blazinblaze.entity;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.*;
import net.blazinblaze.item.custom.FierySnowball;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.phys.Vec3;

public class BCMEntities {

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key));
    }

    private static ResourceKey<EntityType<?>> keyOf(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, id));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static final EntityType<EggNogCow> EGG_NOG_COW = register("egg_nog_cow", EntityType.Builder.<EggNogCow>of(EggNogCow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).eyeHeight(1.3F).passengerAttachments(1.36875F).clientTrackingRange(10));
    public static final EntityType<SnowballVillager> SNOWBALL_VILLAGER = register("snowball_villager", EntityType.Builder.<SnowballVillager>of(SnowballVillager::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<SantaVillager> SANTA_VILLAGER = register("santa_villager", EntityType.Builder.<SantaVillager>of(SantaVillager::new, MobCategory.CREATURE).sized(0.6F, 1.95F).eyeHeight(1.62F).clientTrackingRange(10));
    public static final EntityType<EvilSantaVillager> EVIL_SANTA_VILLAGER = register("evil_santa_villager", EntityType.Builder.<EvilSantaVillager>of(EvilSantaVillager::new, MobCategory.MONSTER).sized(0.6F, 1.95F).passengerAttachments(2.0F).ridingOffset(-0.6F).clientTrackingRange(8));
    public static final EntityType<ElfVex> ELF_VEX = register("elf_vex", EntityType.Builder.<ElfVex>of(ElfVex::new, MobCategory.MONSTER).sized(0.6F, 1.95F).fireImmune().sized(0.4F, 0.8F).eyeHeight(0.51875F).passengerAttachments(0.7375F).ridingOffset(0.04F).clientTrackingRange(8));
    public static final EntityType<FriendlyElfVex> FRIENDLY_ELF_VEX = register("friendly_elf_vex", EntityType.Builder.<FriendlyElfVex>of(FriendlyElfVex::new, MobCategory.CREATURE).sized(0.6F, 1.95F).fireImmune().sized(0.4F, 0.8F).eyeHeight(0.51875F).passengerAttachments(0.7375F).ridingOffset(0.04F).clientTrackingRange(8));
    public static final EntityType<Reindeer> REINDEER = register("reindeer", EntityType.Builder.<Reindeer>of(Reindeer::new, MobCategory.CREATURE).sized(0.9F, 1.87F).eyeHeight(1.7765F).passengerAttachments(new Vec3(0.0, 1.37, -0.3)).clientTrackingRange(10));
    public static final EntityType<GingerbreadMan> GINGERBREAD_MAN = register("gingerbread_man", EntityType.Builder.<GingerbreadMan>of(GingerbreadMan::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8));
    public static final EntityType<FriendlyGingerbreadMan> FRIENDLY_GINGERBREAD_MAN = register("friendly_gingerbread_man", EntityType.Builder.<FriendlyGingerbreadMan>of(FriendlyGingerbreadMan::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8));

    public static final EntityType<FierySnowballEntity> FIERY_SNOWBALL_ENTITY = register("fiery_snowball_entity", EntityType.Builder.<FierySnowballEntity>of(FierySnowballEntity::new, MobCategory.MISC).noLootTable()
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final EntityType<EtherealSnowballEntity> ETHEREAL_SNOWBALL_ENTITY = register("ethereal_snowball_entity", EntityType.Builder.<EtherealSnowballEntity>of(EtherealSnowballEntity::new, MobCategory.MISC).noLootTable()
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final EntityType<WitheredSnowballEntity> WITHERED_SNOWBALL_ENTITY = register("withered_snowball_entity", EntityType.Builder.<WitheredSnowballEntity>of(WitheredSnowballEntity::new, MobCategory.MISC).sized(0.6F, 1.95F).noLootTable()
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final EntityType<CandyCaneSnowballEntity> CANDY_CANE_SNOWBALL_ENTITY = register("candy_cane_snowball_entity", EntityType.Builder.<CandyCaneSnowballEntity>of(CandyCaneSnowballEntity::new, MobCategory.MISC).sized(0.6F, 1.95F).noLootTable()
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));

    public static void initializeAttributes() {
        FabricDefaultAttributeRegistry.register(EGG_NOG_COW, Cow.createAttributes());
        FabricDefaultAttributeRegistry.register(SANTA_VILLAGER, SantaVillager.createAttributes());
        FabricDefaultAttributeRegistry.register(EVIL_SANTA_VILLAGER, EvilSantaVillager.createEvilSantaAttributes());
        FabricDefaultAttributeRegistry.register(ELF_VEX, ElfVex.createAttributes());
        FabricDefaultAttributeRegistry.register(FRIENDLY_ELF_VEX, FriendlyElfVex.createAttributes());
        FabricDefaultAttributeRegistry.register(REINDEER, Reindeer.createBaseChestedHorseAttributes());
        FabricDefaultAttributeRegistry.register(GINGERBREAD_MAN, GingerbreadMan.createAttributes());
        FabricDefaultAttributeRegistry.register(FRIENDLY_GINGERBREAD_MAN, FriendlyGingerbreadMan.createAttributes());
    }

    public static void initializeMobs() {
    }
}
