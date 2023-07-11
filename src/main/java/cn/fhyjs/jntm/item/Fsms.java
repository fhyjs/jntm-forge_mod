package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

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
        if(!player.isCreative()){
            player.getHeldItem(hand).shrink(1); // 数量 - 1
        }
        return EnumActionResult.SUCCESS;

    }

}
