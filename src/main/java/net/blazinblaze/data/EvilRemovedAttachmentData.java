package net.blazinblaze.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EvilRemovedAttachmentData(Boolean aBoolean) {
    public static Codec<EvilRemovedAttachmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("aBoolean").forGetter(EvilRemovedAttachmentData::aBoolean)
    ).apply(instance, EvilRemovedAttachmentData::new));

    public static StreamCodec<ByteBuf, EvilRemovedAttachmentData> PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static EvilRemovedAttachmentData DEFAULT = new EvilRemovedAttachmentData(false);

    public EvilRemovedAttachmentData setEvilRemoved(boolean evilRemoved) {
        return new EvilRemovedAttachmentData(evilRemoved);
    }

    public EvilRemovedAttachmentData clear() {
        return DEFAULT;
    }
}
