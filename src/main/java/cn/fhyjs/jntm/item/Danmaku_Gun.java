package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.entity.Ta_Danmaku;
import cn.fhyjs.jntm.entity.cxk;
import cn.fhyjs.jntm.entity.danmaku.DanmakuColor;
import cn.fhyjs.jntm.entity.danmaku.DanmakuShoot;
import cn.fhyjs.jntm.entity.danmaku.DanmakuType;
import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Danmaku_Gun extends Item {
    public  Danmaku_Gun(){
        this.setRegistryName("danmakugan");
        this.setUnlocalizedName(Jntm.MODID + "danmakugan");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.danmaku_gun.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        {
            return 0;
        }
    }
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        EntityPlayer ep = (EntityPlayer) entityLiving;
        if(ep.getCooldownTracker().hasCooldown(stack.getItem())){
            return true;
        }
        if (!entityLiving.world.isRemote){
            int r=3;
            for (int i=0;i<r;i++){
                for(int j=0;j<r;j++) {
                    for(int k=0;k<r;k++) {
                        Ta_Danmaku danmaku = new Ta_Danmaku(entityLiving.world, entityLiving, 50, 0, DanmakuType.BASKETBALL, DanmakuColor.GREEN.ordinal());
                        danmaku.setLocationAndAngles(danmaku.posX - 1 + i, danmaku.posY + j + 1, danmaku.posZ-1+k, danmaku.rotationYaw, danmaku.rotationPitch);
                        float offset = 0.3f / 2;
                        danmaku.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, offset, 1, 1);
                        entityLiving.world.spawnEntity(danmaku);
                        entityLiving.world.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, entityLiving.getSoundCategory(), 1.0f, 0.8f);
                    }
                }
            }
        }
        ep.getCooldownTracker().setCooldown(this, 20);
        return true;
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!playerIn.isSneaking()) {
            playerIn.getCooldownTracker().setCooldown(this, 4);
            if (!worldIn.isRemote) {
                Ta_Danmaku danmaku = new Ta_Danmaku(worldIn, playerIn, 10, 0, DanmakuType.PELLET, DanmakuColor.GREEN.ordinal());
                float offset = 0.3f / 2;
                danmaku.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, offset, 1, 1);
                worldIn.spawnEntity(danmaku);
            }
        }else {
            playerIn.getCooldownTracker().setCooldown(this, 10);
            if (!worldIn.isRemote) {
                Ta_Danmaku danmaku = new Ta_Danmaku(worldIn, playerIn, 50, 0, DanmakuType.BUBBLE, DanmakuColor.RED.ordinal());
                float offset = 0.3f / 2;
                danmaku.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, offset, 1, 1);
                worldIn.spawnEntity(danmaku);
            }
        }
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, playerIn.getSoundCategory(), 1.0f, 0.8f);
        return new ActionResult<>(EnumActionResult.SUCCESS,playerIn.getHeldItem(handIn));
    }
}
