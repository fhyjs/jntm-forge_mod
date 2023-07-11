package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
}
