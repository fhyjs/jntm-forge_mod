package cn.fhyjs.jntm.item.LandminePlugins;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class CamouflageUpgrade extends Item implements LandminePB {
    public CamouflageUpgrade() {
        super();
        this.setRegistryName("landmine_camouflage_upgrade");
        this.setUnlocalizedName(Jntm.MODID+".landmine_camouflage_upgrade");
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(new TextComponentTranslation("item.landmine_camouflage_upgrade.tip.choose").getFormattedText());
        if (stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("pos")) {
            int[] ia = stack.getTagCompound().getIntArray("pos");
            BlockPos bp = new BlockPos(ia[0],ia[1],ia[2]);
            tooltip.add(String.format("§4%s[%s]",bp,player.getBlockState(bp).getBlock().getLocalizedName()));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        double playerX = player.posX;
        double playerY = player.posY + player.eyeHeight; // 加上眼睛高度
        double playerZ = player.posZ;
        float playerPitch = player.rotationPitch;
        float playerYaw = player.rotationYaw;

        // 通过玩家的视角计算准星的方向向量
        double pitchRadians = Math.toRadians(playerPitch);
        double yawRadians = Math.toRadians(playerYaw);
        double xLookVec = -Math.sin(yawRadians) * Math.cos(pitchRadians);
        double yLookVec = -Math.sin(pitchRadians);
        double zLookVec = Math.cos(yawRadians) * Math.cos(pitchRadians);

        // 设置准星射线的最大距离，可以根据需要进行调整
        double maxDistance = 5;

        // 创建射线结果对象
        RayTraceResult result = world.rayTraceBlocks(new Vec3d(playerX, playerY, playerZ),
                new Vec3d(playerX + xLookVec * maxDistance,
                        playerY + yLookVec * maxDistance,
                        playerZ + zLookVec * maxDistance));
        // 检查射线结果是否命中方块
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos bp=result.getBlockPos();
            // 返回命中的方块位置
            if (world.isRemote)
                player.sendStatusMessage(new TextComponentString(I18n.format("item.landmine_camouflage_upgrade.tip.choosed")+String.format("%s[%s]",bp,world.getBlockState(bp).getBlock().getLocalizedName())),true);
            NBTTagCompound tc = new NBTTagCompound();
            tc.setIntArray("pos",new int[]{bp.getX(),bp.getY(),bp.getZ()});
            player.getHeldItem(hand).setTagCompound(tc);
            return ActionResult.newResult(EnumActionResult.SUCCESS,player.getHeldItem(hand));
        }
        // 如果没有命中方块，返回空
        return ActionResult.newResult(EnumActionResult.FAIL,player.getHeldItem(hand));
    }
}
