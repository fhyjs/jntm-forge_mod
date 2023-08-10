package cn.fhyjs.jntm.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class RenderRope<T extends Entity> extends RenderSnowball<T> {
    public RenderRope(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
        super(renderManagerIn, itemIn, itemRendererIn);
    }
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player != null) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            GlStateManager.pushMatrix();
            GlStateManager.glLineWidth(3);

            bufferBuilder.begin(GL_LINES, DefaultVertexFormats.POSITION);
            bufferBuilder.pos(0,2,0).endVertex();
            bufferBuilder.pos(x,y,z).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
