package net.blazinblaze.networking;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PresentAdvancementC2SPayload(String temp) implements CustomPacketPayload {
    public static final ResourceLocation PRESENT_ADVANCEMENT_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "present_advancement_payload");
    public static final Type<PresentAdvancementC2SPayload> ID = new Type<>(PRESENT_ADVANCEMENT_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PresentAdvancementC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PresentAdvancementC2SPayload::temp, PresentAdvancementC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
