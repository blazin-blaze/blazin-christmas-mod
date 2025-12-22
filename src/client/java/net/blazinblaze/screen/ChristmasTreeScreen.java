package net.blazinblaze.screen;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class ChristmasTreeScreen extends AbstractContainerScreen<ChristmasTreeHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/gui/container/christmas_tree_screen.png");

    public ChristmasTreeScreen(ChristmasTreeHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float deltaTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight + 20, 256, 256);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, Component.literal("Christmas"), this.titleLabelX, this.titleLabelY, CommonColors.WHITE, true);
        context.drawString(this.font, Component.literal("Tree"), this.titleLabelX, this.titleLabelY + 10, CommonColors.WHITE, true);
    }
}
