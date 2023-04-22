package cn.fhyjs.jntm.utility;

import net.katsstuff.teamnightclipse.danmakucore.DanmakuCore;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuState;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.DanmakuTemplate;
import net.katsstuff.teamnightclipse.danmakucore.danmaku.form.Form;
import net.katsstuff.teamnightclipse.danmakucore.data.ShotData;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DanmakuUtils {
    public static void shotDanmaku(EntityLivingBase user,
                                   World worldIn,
                                   Form form){
        DanmakuTemplate.Builder temp = DanmakuTemplate.builder()
                .setUser(user)
                .setWorld(worldIn)
                .setSource(user)
                .setMovementData(1D)
                .setShot(ShotData.DefaultShotData()
                        .setForm(form)
                        .setDamage(2f)

                );
        List<DanmakuState> stateList = new ArrayList<>();
        stateList.add(temp.build().asEntity());
        DanmakuCore.spawnDanmaku(stateList);
    }

    public static Vector3 genLaserVec(Entity entity){
        Vector3 pos_start = Vector3.fromEntityCenter(entity);
        pos_start.multiply(5f);
        return pos_start;
    }
}
