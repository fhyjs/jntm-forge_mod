package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class BlockLandmineConfigurator extends Block {
    public BlockLandmineConfigurator() {
        super(Material.GROUND);
        //初始化
        this.setUnlocalizedName(Jntm.MODID+".LandmineConfigurator");
        //设置UnlocalizedName
        this.setRegistryName("LandmineConfigurator");
        //设置物品ID
        this.setHarvestLevel("pickaxe",0);
        //需要的工具{稿子：pickaxe，剑：sword......}和挖掘等级，这个可以任意选取，钻石镐为3
        this.setHardness(50F);
        //设置硬度，黑曜石是50
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.LandmineConf.getId(), world, (int) pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
    }
}
