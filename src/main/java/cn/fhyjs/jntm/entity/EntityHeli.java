package cn.fhyjs.jntm.entity;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.utility.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityHeli extends EntityLivingBase {
    private boolean keyForward = false;
    private boolean keyJump = false;
    private boolean keyBack = false;
    private boolean keyRun = false;
    private boolean keyLeft = false;
    private boolean keyRight = false;
    private boolean keyForward1 = false;
    private boolean keyJump1 = false;
    private boolean keyBack1 = false;
    private boolean keyRun1 = false;
    private boolean keyLeft1 = false;
    private boolean keyRight1 = false;
    private boolean isReplay = false;
    private boolean vanillaMode = true;
    protected static final DataParameter<NBTTagCompound> LAST_OP_HIS = EntityDataManager.createKey(EntityHeli.class, DataSerializers.COMPOUND_TAG);
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
    public void onUpdate() {
        vanillaMode=false;
        if (vanillaMode)
            bOnUpdate();
        else
            super.onUpdate();
        if (!world.isRemote&& !getPassengers().isEmpty()) {
            if (rand.nextDouble()<0.05)
                world.playSound(null, getPosition(),SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.PLAYERS,.4f,1);
        }
        if (getPassengers().isEmpty()) {
            if (world.isRemote && MediaPlayer.getMp3Player(this) != null) {
                MediaPlayer.getMp3Player(this).stop();
            }
        }
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        super.onDeath(p_70645_1_);
        if (!world.isRemote) {
            world.newExplosion(this,posX,posY,posZ,8,true,true);
        }
        if (world.isRemote && MediaPlayer.getMp3Player(this) != null) {
            MediaPlayer.getMp3Player(this).stop();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LAST_OP_HIS,new NBTTagCompound());
    }
    private void logChange(String key){
        NBTTagCompound nbt = this.dataManager.get(LAST_OP_HIS);
        NBTTagCompound data = new NBTTagCompound();
        switch (key){
            case "f":
                data.setBoolean(key,keyForward);
                break;
            case "b":
                data.setBoolean(key,keyBack);
                break;
            case "l":
                data.setBoolean(key,keyLeft);
                break;
            case "r":
                data.setBoolean(key,keyRight);
                break;
            case "s":
                data.setBoolean(key,keyRun);
                break;
            case "j":
                data.setBoolean(key,keyJump);
                break;
        }
        nbt.setTag(String.valueOf(ticksExisted),data);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("loh",dataManager.get(LAST_OP_HIS));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(LAST_OP_HIS,compound.getCompoundTag("loh"));
    }

    private void readLog(){
        NBTTagCompound nbt = this.dataManager.get(LAST_OP_HIS);
        System.out.println(nbt);
        if (!nbt.hasKey(String.valueOf(this.ticksExisted))) return;
        NBTTagCompound data = nbt.getCompoundTag(String.valueOf(this.ticksExisted));
        if (data.hasKey("f")){
            keyForward=data.getBoolean("f");
            return;
        }
        if (data.hasKey("b")){
            keyBack=data.getBoolean("b");
            return;
        }
        if (data.hasKey("l")){
            keyLeft=data.getBoolean("l");
            return;
        }
        if (data.hasKey("r")){
            keyForward=data.getBoolean("r");
            return;
        }
        if (data.hasKey("s")){
            keyForward=data.getBoolean("s");
            return;
        }
        if (data.hasKey("j")){
            keyForward=data.getBoolean("j");
        }
    }
    private void checkKeys(){
        if (keyForward!=keyForward1){
            keyForward1=keyForward;
            logChange("f");
        }
        if (keyBack!=keyBack1){
            keyBack1=keyBack;
            logChange("b");
        }
        if (keyLeft1!=keyLeft){
            keyLeft1=keyLeft;
            logChange("l");
        }
        if (keyRight1!=keyRight){
            keyRight1=keyRight;
            logChange("r");
        }
        if (keyRun!=keyRun1){
            keyRun1=keyRun;
            logChange("s");
        }
        if (keyJump!=keyJump1){
            keyJump1=keyJump;
            logChange("j");
        }
    }
    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (vanillaMode){
            return;
        }
        Entity entity = getControllingPassenger();
        if (entity instanceof EntityPlayer && this.isBeingRidden()) {
            EntityPlayer player = (EntityPlayer) entity;
            if (world.isRemote) {
                if (player instanceof EntityPlayerSP) {
                    EntityPlayerSP playerSP = (EntityPlayerSP) player;
                    keyForward = playerSP.movementInput.forwardKeyDown;
                    keyBack = playerSP.movementInput.backKeyDown;
                    keyLeft = playerSP.movementInput.leftKeyDown;
                    keyRight = playerSP.movementInput.rightKeyDown;
                    keyJump = playerSP.movementInput.jump;
                    keyRun = player.isSprinting();
                    isReplay = isReply();
                }
            }
            if (!isReplay)
                checkKeys();
            else
                readLog();
            player.fallDistance = 0;
            this.fallDistance = 0;

            this.rotationYaw = player.rotationYaw;
            this.rotationPitch = player.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (keyJump){
                this.move(MoverType.SELF,motionX,motionY+0.3,motionZ);
            }
            if (keyRun){
                this.move(MoverType.SELF,motionX,motionY-0.3,motionZ);
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
        if (!player.isSneaking() && !this.isBeingRidden() && !this.isRiding()) {
            this.dataManager.set(LAST_OP_HIS,new NBTTagCompound());
        }
        if (!player.isSneaking() && !this.world.isRemote && !this.isBeingRidden() && !this.isRiding()) {
            player.startRiding(this);
            return true;
        }
        if (!player.isSneaking() && this.world.isRemote && !this.isBeingRidden() && !this.isRiding()) {
            MediaPlayer.mp3Player(this,Jntm.class.getClassLoader().getResourceAsStream("assets/jntm/sounds/music/laodaxq.mp3"));
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
    @SideOnly(Side.CLIENT)
    private boolean isReply() {
        if (this.getControllingPassenger()==null) return false;
        return !this.getControllingPassenger().equals(Minecraft.getMinecraft().player)    ;
    }
    @SideOnly(Side.CLIENT)
    private static boolean keyRun() {
        return Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown();
    }
    //---------boat update---------
    public void bOnUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        super.onUpdate();
        this.tickLerp();

        if (this.canPassengerSteer())
        {

            this.updateMotion();

            if (this.world.isRemote)
            {
                this.controlBoat();
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
        else
        {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }


        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity = list.get(j);

                if (!entity.isPassenger(this))
                {
                    if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer))
                    {
                        entity.startRiding(this);
                    }
                    else
                    {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private void tickLerp()
    {
        if (this.lerpSteps > 0 && !this.canPassengerSteer())
        {
            double d0 = this.posX + (this.lerpX - this.posX) / (double)this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double)this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double)this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }
    private float momentum;
    private float deltaRotation;
    private void updateMotion()
    {
        if (world.isRemote){
            updateInputs(keyLeft(),keyRight(),keyForward(),keyBack());
        }
        double d0 = -0.03999999910593033D;
        double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;
        this.momentum = 0.05F;

        {

            d2 = (this.getEntityBoundingBox().minY) / (double)this.height;
            this.momentum = 0.9F;


            this.motionX *= (double)this.momentum;
            this.motionZ *= (double)this.momentum;
            this.deltaRotation *= this.momentum;
            this.motionY += d1;

            if (d2 > 0.0D)
            {
                double d3 = 0.65D;
                this.motionY += d2 * 0.06153846016296973D;
                double d4 = 0.75D;
                this.motionY *= 0.75D;
            }
        }
    }
    @SideOnly(Side.CLIENT)
    public void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_)
    {
        this.leftInputDown = p_184442_1_;
        this.rightInputDown = p_184442_2_;
        this.forwardInputDown = p_184442_3_;
        this.backInputDown = p_184442_4_;
    }
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private void controlBoat()
    {
        if (this.isBeingRidden())
        {
            float f = 0.0F;

            if (this.leftInputDown)
            {
                this.deltaRotation += -1.0F;
            }

            if (this.rightInputDown)
            {
                ++this.deltaRotation;
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown)
            {
                f += 0.005F;
            }

            this.rotationYaw += this.deltaRotation;

            if (this.forwardInputDown)
            {
                f += 0.04F;
            }

            if (this.backInputDown)
            {
                f -= 0.005F;
            }

            this.motionX += (double)(MathHelper.sin(-this.rotationYaw * 0.017453292F) * f);
            this.motionZ += (double)(MathHelper.cos(this.rotationYaw * 0.017453292F) * f);
        }
    }
}
