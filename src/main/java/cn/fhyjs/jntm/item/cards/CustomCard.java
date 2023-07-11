package cn.fhyjs.jntm.item.cards;

import cn.fhyjs.jntm.item.CanShotBase;
import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.subentity.SubEntityType;
import net.katsstuff.teamnightclipse.danmakucore.data.ShotData;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Collections;

public abstract class CustomCard extends CanShotBase {
    public CustomCard(String name, CreativeTabs tab) {
        super(name, tab);
    }
    public abstract SubEntityType getSubentity();
    public DanmakuTemplate.Builder reShape(DanmakuTemplate.Builder builder){
        return builder;
    }
    @Override
    public void onShotUse(World worldIn, EntityPlayer player, EnumHand handIn) {
        super.onShotUse(worldIn, player, handIn);
        //if (player.getHeldItemMainhand().getItem() instanceof WeaponBase){
        //    return;
        //}
        DanmakuTemplate.Builder temp = DanmakuTemplate.builder()
                .setUser(player)
                .setWorld(worldIn)
                .setSource(player)
                .setMovementData(1D)
                .setShot(ShotData.DefaultShotData()
                        .setForm(LibForms.TALISMAN)
                        .setDamage(2f)
                        .setSubEntity(getSubentity())
                );
        DanmakuCore.spawnDanmaku(Collections.singletonList(reShape(temp).build().asEntity()));
    }
}
