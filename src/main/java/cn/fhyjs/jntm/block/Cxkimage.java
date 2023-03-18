package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.renderer.IItemWithMeshDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect;

public class Cxkimage extends BlockTileEntity<TileEntityCxkImage> {

    public Cxkimage() {
        super(Material.WOOD,new TileEntityCxkImage());
        this.setRegistryName("cxkimages");
        this.setUnlocalizedName(Jntm.MODID + "cxkimages");
        this.setCreativeTab(jntm_Group);
        this.setHarvestLevel("pickaxe",0);
        this.setHardness(2F);

    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        //getTileEntity(worldIn,pos).count="not_set";
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityCxkImage tile = getTileEntity(world, pos);
            Jntm.logger.error("Count: " + tile.getCount());
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.SetCxkimage.getId(), world, (int) pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
    }

    @Override
    public Class<TileEntityCxkImage> getTileEntityClass() {
        return TileEntityCxkImage.class;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @Nullable
    @Override
    public TileEntityCxkImage createTileEntity(World world, IBlockState state) {
        return new TileEntityCxkImage();
    }

}