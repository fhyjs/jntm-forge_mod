package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.EventHandler;
import cn.fhyjs.jntm.screen.Screen;
import cn.fhyjs.jntm.screen.ScreenM;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class ItemScreenBuilder extends Item {
    public ItemScreenBuilder() {
        super();
        this.setRegistryName("screenbuider");
        this.setUnlocalizedName(Jntm.MODID + ".screenbuider");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(I18n.format("tooltip.screenbuider.n1"));
        super.addInformation(stack, player, tooltip, advanced);
    }
    RayTraceResult traceResult;
    BlockPos bpS,bpE;
    EnumFacing.Axis facing;
    int minX,minY,minZ,maxX,maxY,maxZ;
    @SideOnly(Side.CLIENT)
    int bpPosC=-1,bpPosE=-1;
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!isSelected) {
            if (bpPosC!=-1){
                updateList(EventHandler.poses,bpPosC,null);
                if (world.isRemote)
                    EventHandler.poses.clear();
                bpPosC=-1;
                bpPosE=-1;
            }
            return;
        }
        if (entityIn instanceof EntityPlayer){
            double playerX = entityIn.posX;
            double playerY = entityIn.posY + entityIn.getEyeHeight(); // 加上眼睛高度
            double playerZ = entityIn.posZ;
            float playerPitch = entityIn.rotationPitch;
            float playerYaw = entityIn.rotationYaw;
            // 通过玩家的视角计算准星的方向向量
            double pitchRadians = Math.toRadians(playerPitch);
            double yawRadians = Math.toRadians(playerYaw);
            double xLookVec = -Math.sin(yawRadians) * Math.cos(pitchRadians);
            double yLookVec = -Math.sin(pitchRadians);
            double zLookVec = Math.cos(yawRadians) * Math.cos(pitchRadians);

            // 设置准星射线的最大距离，可以根据需要进行调整
            double maxDistance = 5;

            // 创建射线结果对象
            traceResult = world.rayTraceBlocks(new Vec3d(playerX, playerY, playerZ),
                    new Vec3d(playerX + xLookVec * maxDistance,
                            playerY + yLookVec * maxDistance,
                            playerZ + zLookVec * maxDistance),true,false,true);
            if (traceResult==null||!traceResult.typeOfHit.equals(RayTraceResult.Type.MISS)) return;
            if (stack.getTagCompound()==null||!stack.getTagCompound().hasKey("posS")) {
                bpS = traceResult.getBlockPos();
                if (world.isRemote) {
                    if (bpPosC == -1)
                        bpPosC = EventHandler.poses.size();
                    updateList(EventHandler.poses, bpPosC, bpS);
                }
            }else if (!stack.getTagCompound().hasKey("posE")){
                bpS=readBlockFromNBT(stack.getTagCompound(),"posS");
                bpE=traceResult.getBlockPos();

                if (world.isRemote)
                    if (bpPosC != -1)
                        if (bpPosE != -1) {
                            EventHandler.poses.clear();
                            bpPosC=-1;
                            bpPosE=-1;
                        }

                if(bpS.getX() == bpE.getX()){
                    facing=EnumFacing.Axis.X;
                }else if(bpS.getY() == bpE.getY()){
                    facing=EnumFacing.Axis.Y;
                }else if(bpS.getZ() == bpE.getZ()){
                    facing=EnumFacing.Axis.Z;
                } else facing=null;

                 minX = Math.min(bpS.getX(), bpE.getX());
                 minY = Math.min(bpS.getY(), bpE.getY());
                 minZ = Math.min(bpS.getZ(), bpE.getZ());

                 maxX = Math.max(bpS.getX(), bpE.getX());
                 maxY = Math.max(bpS.getY(), bpE.getY());
                 maxZ = Math.max(bpS.getZ(), bpE.getZ());

                List<BlockPos> list= new ArrayList<>();
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            BlockPos currentPos = new BlockPos(x, y, z);
                            list.add(currentPos);
                        }
                    }
                }

                if (world.isRemote) {
                    if (bpPosC == -1)
                        bpPosC = EventHandler.poses.size();
                    if (bpPosE == -1)
                        bpPosE = bpPosC+list.size();
                    EventHandler.poses.addAll(bpPosC,list);
                }
            }

        }
        super.onUpdate(stack, world, entityIn, itemSlot, isSelected);
    }
    public static void writeBlockPos2NBT(NBTTagCompound nbt,String name,BlockPos bp){
        nbt.setIntArray(name,new int[]{bp.getX(),bp.getY(),bp.getZ()});
    }
    public static BlockPos readBlockFromNBT(NBTTagCompound nbt,String name){
        if (nbt==null||!nbt.hasKey(name)) return null;
        int[] ia=nbt.getIntArray(name);
        return new BlockPos(ia[0],ia[1],ia[2]);
    }
    public <T>void updateList(List<T> list, int index, T value){
        while (list.size()<=index)
            list.add(null);
        list.set(index,value);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)){
            Screen screen=ScreenM.getInstance().screenAt(traceResult.getBlockPos());
            if (screen!=null){
                if (worldIn.isRemote){
                    if (!playerIn.isSneaking()) {
                        playerIn.sendMessage(new TextComponentString(String.format("Selected a screen.(id : %d)", screen.getId())).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                        playerIn.sendMessage(new TextComponentString(String.format("Press Shift and click it again to delete this screen.", screen.getId())).setStyle(new Style().setColor(TextFormatting.AQUA)));
                    }else {
                        playerIn.sendMessage(new TextComponentString("Success!").setStyle(new Style().setColor(TextFormatting.GREEN)));
                    }
                }else {
                    if (playerIn.isSneaking()) {
                        ScreenM.getInstance().rmScreen(screen);
                    }
                }
                return ActionResult.newResult(EnumActionResult.SUCCESS,stack);
            }
            return ActionResult.newResult(EnumActionResult.PASS,stack);
        }
        if (playerIn.isSneaking()){
            if (worldIn.isRemote) {
                playerIn.sendMessage(new TextComponentString("Canceled").setStyle(new Style().setColor(TextFormatting.RED)));
                EventHandler.poses.clear();
            }
            stack.setTagCompound(new NBTTagCompound());
            return ActionResult.newResult(EnumActionResult.SUCCESS,stack);
        }
        if (stack.getTagCompound()==null||!stack.getTagCompound().hasKey("posS")) {
            NBTTagCompound tag = new NBTTagCompound();
            writeBlockPos2NBT(tag,"posS",bpS);
            stack.setTagCompound(tag);
            return ActionResult.newResult(EnumActionResult.SUCCESS,stack);
        }else if (!stack.getTagCompound().hasKey("posE")){
            if (facing==null) {
                if (worldIn.isRemote)
                    playerIn.sendMessage(new TextComponentTranslation("tip.jntm.item.sb.fail.thick").setStyle(new Style().setColor(TextFormatting.RED)));
                return ActionResult.newResult(EnumActionResult.FAIL,stack);
            }
            int height = 0,width = 0;
            switch (facing) {
                case X:
                    height=Math.abs(maxY-minY);
                    width=Math.abs(maxZ-minZ);
                    break;
                case Y:
                    height=Math.abs(maxX-minX);
                    width=Math.abs(maxZ-minZ);
                    break;
                case Z:
                    height=Math.abs(maxY-minY);
                    width=Math.abs(maxX-minX);
                    break;
            }
            height++;
            width++;
            if (worldIn.isRemote){
                playerIn.sendMessage(new TextComponentString(String.format("Ready to construction (facing : %s, width : %d, height : %d)",facing,width,height)).setStyle(new Style().setColor(TextFormatting.GREEN)));
                playerIn.sendMessage(new TextComponentString("Click again to construction").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                playerIn.sendMessage(new TextComponentString("Shift + click to cancel").setStyle(new Style().setColor(TextFormatting.RED)));
            }
            writeBlockPos2NBT(stack.getTagCompound(),"posE",bpE);
            stack.getTagCompound().setString("facing",facing.toString());
            stack.getTagCompound().setInteger("width",width);
            stack.getTagCompound().setInteger("height",height);
            writeBlockPos2NBT(stack.getTagCompound(),"pos",new BlockPos(minX,minY,minZ));
        }else {
            if (!worldIn.isRemote) ScreenM.getInstance().addScreen(
                    ScreenM.getInstance().Build(
                        readBlockFromNBT(stack.getTagCompound(),"pos"),
                        stack.getTagCompound().getInteger("width"),
                        stack.getTagCompound().getInteger("height"),
                        EnumFacing.Axis.byName(stack.getTagCompound().getString("facing")),
                        worldIn
                    ));
            if (worldIn.isRemote) playerIn.sendMessage(new TextComponentString("Success!").setStyle(new Style().setColor(TextFormatting.GREEN)));
            if (worldIn.isRemote) EventHandler.poses.clear();
            stack.setTagCompound(new NBTTagCompound());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}