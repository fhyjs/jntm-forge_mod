package cn.fhyjs.jntm.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.layer.GenLayerSmooth;

import static net.minecraft.client.renderer.GlStateManager.glBegin;
import static net.minecraft.client.renderer.GlStateManager.glEnd;
import static org.lwjgl.opengl.GL11.*;

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
            bufferBuilder.pos(0,2,0).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
            bufferBuilder.pos(x,y,z).color(0.0f, 1.0f, 0.0f, 1.0f).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
