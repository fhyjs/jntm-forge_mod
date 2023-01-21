package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.CxkTnt_E;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class CxkTNT extends BlockTNT {
    public CxkTNT() {
        super();
        //初始化
        this.setUnlocalizedName(Jntm.MODID+"cxktnt");
        //设置UnlocalizedName
        this.setRegistryName("cxktnt");
        //设置物品ID
        this.setHarvestLevel("pickaxe",0);
        //需要的工具{稿子：pickaxe，剑：sword......}和挖掘等级，这个可以任意选取，钻石镐为3
        this.setHardness(0.1F);
        //设置硬度，黑曜石是50
        this.setCreativeTab(jntm_Group);
        this.setLightLevel((float)2/(float)16);
        this.setLightOpacity(7);
    }
    @Override
    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(EXPLODE)).booleanValue())
            {
                CxkTnt_E cxkTntE = new CxkTnt_E(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter,2f);
                worldIn.spawnEntity(cxkTntE);
                worldIn.playSound((EntityPlayer)null, cxkTntE.posX, cxkTntE.posY, cxkTntE.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

}
