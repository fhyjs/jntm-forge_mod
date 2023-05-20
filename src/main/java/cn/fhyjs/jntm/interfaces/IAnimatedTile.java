package cn.fhyjs.jntm.interfaces;

import net.minecraftforge.common.animation.Event;

public interface IAnimatedTile {

    public void handleEvents(float time, Iterable<Event> pastEvents);

}
