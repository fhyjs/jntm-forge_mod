package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class rawkr extends ItemFood {
    public rawkr(int hungerHeal, float saturation, boolean isWolfFood) {
        super(hungerHeal, saturation, isWolfFood);
        this.setRegistryName("rawkr");
        this.setUnlocalizedName(Jntm.MODID + "rawkr");
        this.setMaxStackSize(64);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        // 会在玩家食用之后被调用，原版金苹果在这里追加多种药水效果
    }
}
