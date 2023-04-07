package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Ji_Armor extends ItemArmor implements ISpecialArmor {
    public static ArmorMaterial INFUSED_ARMOR = EnumHelper.addArmorMaterial("INFUSED_ARMOR", "jntm:textures/models/ji_armor", 200, new int[] {4, 9, 7, 4}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 10.0F);
    private String shot;
    /**
     * @param slot 这片护甲穿哪？头上？身上？腿上？脚上？Enum 类型。
     */
    public Ji_Armor(EntityEquipmentSlot slot) {
        super(INFUSED_ARMOR, 0 ,slot);
        this.setRegistryName("ji_armor_"+slot.getName());
        this.shot=slot.getName();
        this.setUnlocalizedName(Jntm.MODID + "ji_armor_"+slot.getName());
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.jntmji_armor.desc1")+I18n.format("item.jntmji_armor_"+shot+".name")+I18n.format("item.jntmji_armor.desc2"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return null;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack item) {
        // 穿在身上的时候的每时每刻都会调用的方法，可以用来追加药水效果什么的
    }

    // ISpecialArmor 接口实现开始

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(0, 1.0, 100);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }

    // ISpecialArmor 接口实现结束
}
