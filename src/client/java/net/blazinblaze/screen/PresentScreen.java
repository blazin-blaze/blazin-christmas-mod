package net.blazinblaze.screen;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.custom.PresentBlockEntity;
import net.blazinblaze.data.BCMAttachmentTypes;
import net.blazinblaze.data.LastPresentAttachmentData;
import net.blazinblaze.networking.PresentSignerC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.awt.*;
import java.util.Optional;

public class PresentScreen extends AbstractContainerScreen<PresentHandler> {

    private static final ResourceLocation WHITE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/white_present_screen.png");
    private static final ResourceLocation LIGHT_GRAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/light_gray_present_screen.png");
    private static final ResourceLocation GRAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/gray_present_screen.png");
    private static final ResourceLocation BLACK_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/black_present_screen.png");
    private static final ResourceLocation BROWN_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/brown_present_screen.png");
    private static final ResourceLocation RED_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/red_present_screen.png");
    private static final ResourceLocation ORANGE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/orange_present_screen.png");
    private static final ResourceLocation YELLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/yellow_present_screen.png");
    private static final ResourceLocation LIME_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/lime_present_screen.png");
    private static final ResourceLocation GREEN_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/green_present_screen.png");
    private static final ResourceLocation CYAN_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/cyan_present_screen.png");
    private static final ResourceLocation LIGHT_BLUE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/light_blue_present_screen.png");
    private static final ResourceLocation BLUE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/blue_present_screen.png");
    private static final ResourceLocation PURPLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/purple_present_screen.png");
    private static final ResourceLocation MAGENTA_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/magenta_present_screen.png");
    private static final ResourceLocation PINK_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/pink_present_screen.png");
    private final ResourceLocation TEXTURE;
    private final String signer;
    private final boolean isFirstTime;
    private boolean hasSetSigner;
    private MultiLineEditBox widget;

    public PresentScreen(PresentHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);

        Player player = inventory.player;
        BlockPos pos = player.getAttachedOrCreate(BCMAttachmentTypes.LAST_PRESENT_ATTACHMENT_TYPE, () -> LastPresentAttachmentData.DEFAULT).pos();
        net.minecraft.world.level.block.entity.BlockEntity entity = player.level().getBlockEntity(pos);
        if(entity instanceof PresentBlockEntity presentBlockEntity) {
            ClientboundBlockEntityDataPacket packet = (ClientboundBlockEntityDataPacket) presentBlockEntity.getUpdatePacket();
            if(packet != null) {
                Optional<String> signer = packet.getTag().getString("gifterUUID");
                Optional<String> colorType = packet.getTag().getString("colorType");
                if(colorType.isPresent()) {
                    DyeColor color = DyeColor.byName(colorType.get(), null);
                    this.TEXTURE = switch (color) {
                        case WHITE -> WHITE_TEXTURE;
                        case ORANGE -> ORANGE_TEXTURE;
                        case MAGENTA -> MAGENTA_TEXTURE;
                        case LIGHT_BLUE -> LIGHT_BLUE_TEXTURE;
                        case YELLOW -> YELLOW_TEXTURE;
                        case LIME -> LIME_TEXTURE;
                        case PINK -> PINK_TEXTURE;
                        case GRAY -> GRAY_TEXTURE;
                        case LIGHT_GRAY -> LIGHT_GRAY_TEXTURE;
                        case CYAN -> CYAN_TEXTURE;
                        case BLUE -> BLUE_TEXTURE;
                        case BROWN -> BROWN_TEXTURE;
                        case GREEN -> GREEN_TEXTURE;
                        case RED -> RED_TEXTURE;
                        case BLACK -> BLACK_TEXTURE;
                        case PURPLE -> PURPLE_TEXTURE;
                    };
                }else {
                    this.TEXTURE = LIGHT_GRAY_TEXTURE;
                }
                if(signer.isPresent()) {
                    String string = signer.get();
                    this.signer = string;
                    if(string.matches("test")) {
                        this.isFirstTime = true;
                    }else {
                        this.isFirstTime = false;
                    }
                }else {
                    this.signer = "test";
                    this.isFirstTime = true;
                }
            }else {
                this.TEXTURE = LIGHT_GRAY_TEXTURE;
                this.signer = "test";
                this.isFirstTime = true;
            }
        }else {
            this.TEXTURE = LIGHT_GRAY_TEXTURE;
            this.signer = "test";
            this.isFirstTime = true;
        }
        this.hasSetSigner = false;
    }

    @Override
    protected void renderBg(GuiGraphics context, float deltaTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight + 20, 256, 256);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        if(this.isFirstTime) {
            this.widget = MultiLineEditBox.builder().setTextColor(CommonColors.WHITE).setShowDecorations(true).setShowBackground(true).setPlaceholder(Component.literal("Signing Name")).setX(leftPos + 178).setY(topPos + 40).build(this.font, 60, 20, Component.literal("Sign your name here!"));
            this.widget.setCharacterLimit(8);
            this.widget.setLineLimit(1);
            this.widget.setFocused(true);

            Button buttonWidget = Button.builder(Component.nullToEmpty("Sign"), (btn) -> {
                // When the button is clicked, we can display a toast to the screen.

                PresentSignerC2SPayload payload = new PresentSignerC2SPayload(this.widget.getValue() != null ? this.widget.getValue() : "test");
                ClientPlayNetworking.send(payload);
                this.hasSetSigner = true;
                this.onClose();

                //this.client.getToastManager().add(
                //SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
                //);
            }).bounds(leftPos + 178, topPos + 15, 60, 20).build();

            // x, y, width, height
            // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
            // textures.

            // Register the button widget.
            this.addRenderableWidget(this.widget);
            this.addRenderableWidget(buttonWidget);
        }
    }

    @Override
    protected void setInitialFocus() {
        if(this.widget != null) {
            this.setInitialFocus(this.widget);
        }else {
            super.setInitialFocus();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(this.widget != null) {
            return !this.widget.keyPressed(keyCode, scanCode, modifiers) && !this.widget.isFocused() ? super.keyPressed(keyCode, scanCode, modifiers) : true;
        }else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        int color;
        if(TEXTURE.equals(GRAY_TEXTURE) ||
                TEXTURE.equals(BLACK_TEXTURE) ||
                TEXTURE.equals(BROWN_TEXTURE) ||
                TEXTURE.equals(RED_TEXTURE) ||
                TEXTURE.equals(GREEN_TEXTURE) ||
                TEXTURE.equals(CYAN_TEXTURE) ||
                TEXTURE.equals(BLUE_TEXTURE) ||
                TEXTURE.equals(PURPLE_TEXTURE) ||
                TEXTURE.equals(MAGENTA_TEXTURE)) {
            color = CommonColors.WHITE;
        }else {
            color = CommonColors.LIGHT_GRAY;
        }
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color, true);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color, true);
        context.drawString(this.font, Component.literal("From:").withStyle(ChatFormatting.BOLD), 98, 28, CommonColors.DARK_GRAY, false);
        if(this.isFirstTime) {
            context.drawString(this.font, Component.literal("YOU!").withStyle(ChatFormatting.BOLD), 98, 41, CommonColors.DARK_GRAY, false);
        }else {
            context.drawString(this.font, Component.literal(signer).withStyle(ChatFormatting.BOLD), 98, 41, CommonColors.DARK_GRAY, false);
        }
    }

    @Override
    public void onClose() {
        if(!hasSetSigner && isFirstTime) {
            PresentSignerC2SPayload payload = new PresentSignerC2SPayload("Nobody");
            ClientPlayNetworking.send(payload);
        }
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !this.isFirstTime;
    }

}
