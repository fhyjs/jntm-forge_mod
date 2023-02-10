package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.katsstuff.danmakucore.item.ItemDanmaku;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
import static net.minecraft.item.ItemMonsterPlacer.applyItemEntityDataToEntity;

public class Fsms extends Item {
    String s1;
    Entity e;
    public Fsms(String enti){
        this.setRegistryName("fsms_"+enti);
        this.setUnlocalizedName("entity."+enti+".name");
        this.setMaxStackSize(64);
        this.setCreativeTab(jntm_Group);
        s1=enti;
    }
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + I18n.translateToLocal("spawn.name")).trim();

        if (s1 != null)
        {
            s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        e=EntityList.createEntityByIDFromName(new ResourceLocation(Jntm.MODID,s1), worldIn);
        BlockPos blockpos = pos.offset(facing);

        e.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 1, (double)blockpos.getZ() + 0.5D,MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        worldIn.spawnEntity(e);

        return EnumActionResult.SUCCESS;

    }

}
