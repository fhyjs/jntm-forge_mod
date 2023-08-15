package cn.fhyjs.jntm.entity;

import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.util.EventReaderDelegate;
import java.util.Collections;

public class EntityHeli extends EntityLivingBase {
    private boolean keyForward = false;
    private boolean keyJump = false;
    private boolean keyBack = false;
    private boolean keyLeft = false;
    private boolean keyRight = false;
    public EntityHeli(World worldIn) {
        super(worldIn);
        this.setSize(2, 2.5625F);
    }

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(@Nonnull EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(@Nonnull EntityEquipmentSlot slotIn, @Nonnull ItemStack stack) {
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.LEFT;
    }

    @Override
    public boolean canPassengerSteer() {
        return true;
    }
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }
    @Override
    public void travel(float strafe, float vertical, float forward) {
        Entity entity = getControllingPassenger();
        if (entity instanceof EntityPlayer && this.isBeingRidden()) {
            EntityPlayer player = (EntityPlayer) entity;

            player.fallDistance = 0;
            this.fallDistance = 0;

            this.rotationYaw = player.rotationYaw;
            this.rotationPitch = player.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (world.isRemote) {
                keyForward = keyForward();
                keyBack = keyBack();
                keyLeft = keyLeft();
                keyRight = keyRight();
                keyJump = keyJump();
            }
            if (keyJump){
                this.move(MoverType.SELF,motionX,motionY+0.3,motionZ);
            }
            strafe = keyLeft ? 0.5f : (keyRight ? -0.5f : 0);
            vertical = keyForward ? -(player.rotationPitch - 10) / 45 : 0;
            forward = keyForward ? 3 : (keyBack ? -0.5f : 0);

            this.moveRelative(strafe, vertical, forward, 0.02f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else if (entity == null && !this.isRiding() && !this.onGround) {
            super.travel(0, -0.3f, 0);
        } else {
            super.travel(strafe, vertical, forward);
        }
    }
    /**
     * 处理交互
     */
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).getItem() == Items.NAME_TAG) {
            // 返回 false，交由玩家侧的右击事件进行处理
            return false;
        }
        if (!player.isSneaking() && !this.world.isRemote && !this.isBeingRidden() && !this.isRiding()) {
            player.startRiding(this);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }
    @Override
    public void updatePassenger(Entity entity) {
        if (!getPassengers().isEmpty()) {
            // 设置乘客实体的位置，相对于主要实体的位置
            for (int i = 0; i < getPassengers().size(); i++) {
                Entity e = getPassengers().get(i);
                double h = 0;
                if (getPassengers().size()>1){
                    h=getPassengers().get(i-1).posY+getPassengers().get(i-1).height;
                }
                e.setPosition(posX,posY+0.2+h,posZ);
            }
        }
    }
    /**
     * 方向同步
     */
    @Override
    public boolean shouldRiderFaceForward(EntityPlayer player) {
        return true;
    }
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    @SideOnly(Side.CLIENT)
    private static boolean keyForward() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyBack() {
        return Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyLeft() {
        return Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyRight() {
        return Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
    }
    @SideOnly(Side.CLIENT)
    private static boolean keyJump() {
        return Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
    }
}
