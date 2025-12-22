package net.blazinblaze.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SantaSpawnChanceAttachmentData(Integer integer) {
    public static Codec<SantaSpawnChanceAttachmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("integer").forGetter(SantaSpawnChanceAttachmentData::integer)
    ).apply(instance, SantaSpawnChanceAttachmentData::new));

    public static StreamCodec<ByteBuf, SantaSpawnChanceAttachmentData> PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static SantaSpawnChanceAttachmentData DEFAULT = new SantaSpawnChanceAttachmentData(24000);

    public SantaSpawnChanceAttachmentData setChance(int chance) {
        return new SantaSpawnChanceAttachmentData(chance);
    }

    public SantaSpawnChanceAttachmentData clear() {
        return DEFAULT;
    }
}
