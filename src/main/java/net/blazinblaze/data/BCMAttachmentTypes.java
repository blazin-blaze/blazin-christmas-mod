package net.blazinblaze.data;

import net.blazinblaze.BlazinChristmasMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

public class BCMAttachmentTypes {
    public static final AttachmentType<LastPresentAttachmentData> LAST_PRESENT_ATTACHMENT_TYPE = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID,"last_present_data"),
            builder->builder // we are using a builder chain here to configure the attachment data type
                    .initializer(()->LastPresentAttachmentData.DEFAULT) // a default value to provide if you dont supply one
                    .persistent(LastPresentAttachmentData.CODEC) // how to save and load the data when the object it is attached to is saved or loaded
                    .syncWith(
                            LastPresentAttachmentData.PACKET_CODEC,  // how to turn the data into a packet to send to players
                            AttachmentSyncPredicate.all() // who to send the data to
                    ));

    public static final AttachmentType<EvilRemovedAttachmentData> EVIL_REMOVED_ATTACHMENT_TYPE = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID,"evil_removed_data"),
            builder->builder // we are using a builder chain here to configure the attachment data type
                    .initializer(()->EvilRemovedAttachmentData.DEFAULT) // a default value to provide if you dont supply one
                    .persistent(EvilRemovedAttachmentData.CODEC) // how to save and load the data when the object it is attached to is saved or loaded
                    .syncWith(
                            EvilRemovedAttachmentData.PACKET_CODEC,  // how to turn the data into a packet to send to players
                            AttachmentSyncPredicate.all() // who to send the data to
                    ));

    public static void initialize() {}
}
