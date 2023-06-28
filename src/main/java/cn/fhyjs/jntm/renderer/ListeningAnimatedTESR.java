package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.interfaces.IAnimatedTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;

public class ListeningAnimatedTESR<TE extends TileEntity & IAnimatedTile> extends AnimationTESR<TE>{

    @Override
    public void handleEvents(TE te, float time, Iterable<Event> pastEvents) {
        te.handleEvents(time, pastEvents);
    }

}
