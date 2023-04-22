package cn.fhyjs.jntm.item.weapon;

import cn.fhyjs.jntm.item.CanShotBase;
import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.lib.LibColor;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibShotData;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Collections;

public class GossipStove extends CanShotBase {
    public GossipStove(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public void onShotUse(World worldIn, EntityPlayer player, EnumHand handIn) {
        super.onShotUse(worldIn, player, handIn);
        DanmakuTemplate.Builder temp = DanmakuTemplate.builder()
                .setSource(player)
                .setUser(player)
                .setWorld(worldIn)
                .setMovementData(0D)
                .setShot(LibShotData.SHOT_LASER
                        .setCoreColor(LibColor.COLOR_VANILLA_WHITE)
                        .setEdgeColor(LibColor.COLOR_SATURATED_YELLOW)
                        .setDamage(2f)
                        .scaleSize(10f)
                );
        Vector3 start_vec = new Vector3(player)
                .offset(new Vector3(player.getLookVec()).normalize(),80);
        temp.setPos(start_vec);
        DanmakuCore.spawnDanmaku(Collections.singletonList(temp.build().asEntity()));
    }
}
