package cn.fhyjs.jntm.compat.tlm;

import cn.fhyjs.jntm.item.ItemRope;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BaubleRopeEmptier implements IMaidBauble {
    @Override
    public void onTick(AbstractEntityMaid entityMaid, ItemStack baubleItem) {
        IMaidBauble.super.onTick(entityMaid,baubleItem);
        for (int i = 0; i < entityMaid.getAvailableInv(true).getSlots(); i++) {
            ItemStack is = entityMaid.getAvailableInv(true).getStackInSlot(i);
            if (is.getItem() instanceof ItemRope) {
                if (is.getTagCompound()!=null)
                    if (is.getTagCompound().hasKey("data"))
                        is.setTagCompound(new NBTTagCompound());
            }
        }
    }

}
