package net.blazinblaze.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SantaSpawnDelayAttachmentData(Integer integer) {
    public static Codec<SantaSpawnDelayAttachmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("integer").forGetter(SantaSpawnDelayAttachmentData::integer)
    ).apply(instance, SantaSpawnDelayAttachmentData::new));

    public static StreamCodec<ByteBuf, SantaSpawnDelayAttachmentData> PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static SantaSpawnDelayAttachmentData DEFAULT = new SantaSpawnDelayAttachmentData(24000);

    public SantaSpawnDelayAttachmentData setDelay(int delay) {
        return new SantaSpawnDelayAttachmentData(delay);
    }

    public SantaSpawnDelayAttachmentData clear() {
        return DEFAULT;
    }
}
