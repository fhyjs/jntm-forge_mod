package cn.fhyjs.jntm.enums;

import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.util.EnumHelper;

public class Actions {
    public static HoverEvent.Action SHOW_IMAGE = EnumHelper.addEnum(HoverEvent.Action.class,"SHOW_IMAGE",new Class[]{String.class,boolean.class},"show_image",true);
}
