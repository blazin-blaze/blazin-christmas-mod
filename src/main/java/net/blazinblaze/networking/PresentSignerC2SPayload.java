package net.blazinblaze.networking;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PresentSignerC2SPayload(String signer) implements CustomPacketPayload {
    public static final ResourceLocation PRESENT_SIGNER_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "present_signer_payload");
    public static final Type<PresentSignerC2SPayload> ID = new Type<>(PRESENT_SIGNER_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PresentSignerC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PresentSignerC2SPayload::signer, PresentSignerC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
