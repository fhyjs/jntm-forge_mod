package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.EventHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    BlockPos bp;
    @SideOnly(Side.CLIENT)
    int bpPosC;
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!isSelected) return;
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
            bp = traceResult.getBlockPos();
            if (world.isRemote){
                bpPosC=EventHandler.poses.size();
                EventHandler.poses.add(bpPosC,bp);
            }

        }
        super.onUpdate(stack, world, entityIn, itemSlot, isSelected);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}