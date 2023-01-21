package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.registry.JntmLootTableList;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;

import static cn.fhyjs.jntm.registry.ItemRegistryHandler.ggxdd;
import static cn.fhyjs.jntm.registry.ItemRegistryHandler.rawkr;

public class cxk extends EntityChicken{
    public cxk(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEventRegistryHandler.ji;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEventRegistryHandler.cxk_hurt;
    }
    @Override
    public cxk createChild(EntityAgeable ageable)
    {
        return new cxk(this.world);
    }
    @Override
    protected SoundEvent getDeathSound() {return SoundEventRegistryHandler.cxk_die;}
    @Override
    protected ResourceLocation getLootTable()
    {
        return JntmLootTableList.ENTITIES_CXK;
    }
    @Override
    protected Item getDropItem()
    {
        return rawkr;
    }
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound(SoundEventRegistryHandler.ji, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(ggxdd, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }
}
