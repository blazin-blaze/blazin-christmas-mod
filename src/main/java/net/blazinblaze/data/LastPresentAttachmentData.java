package net.blazinblaze.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record LastPresentAttachmentData(BlockPos pos) {
    public static Codec<LastPresentAttachmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("blockpos").forGetter(LastPresentAttachmentData::pos)
    ).apply(instance, LastPresentAttachmentData::new));

    public static StreamCodec<ByteBuf, LastPresentAttachmentData> PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static LastPresentAttachmentData DEFAULT = new LastPresentAttachmentData(new BlockPos(0,0,0));

    public LastPresentAttachmentData setBlockPos(BlockPos blockPos) {
        return new LastPresentAttachmentData(blockPos);
    }

    public LastPresentAttachmentData clear() {
        return DEFAULT;
    }
}
