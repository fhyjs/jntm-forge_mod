package cn.fhyjs.jntm.item;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class music_xjj extends ItemRecord {
    private static final Map<SoundEvent, ItemRecord> RECORDS = Maps.<SoundEvent, ItemRecord>newHashMap();
    private final SoundEvent sound;
    private final String displayName;
    public music_xjj(String p_i46742_1_, SoundEvent soundIn) {
        super(p_i46742_1_, soundIn);
        this.setRegistryName("record_"+p_i46742_1_);
        this.setUnlocalizedName(Jntm.MODID + "record_xjj"+p_i46742_1_);
        this.setMaxStackSize(1);
        this.setCreativeTab(jntm_Group);
        this.displayName = "item.record." + p_i46742_1_ + ".desc";
        this.sound = soundIn;
        this.maxStackSize = 1;
        RECORDS.put(this.sound, this);
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == Blocks.JUKEBOX && !((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue())
        {
            if (!worldIn.isRemote)
            {
                ItemStack itemstack = player.getHeldItem(hand);
                ((BlockJukebox)Blocks.JUKEBOX).insertRecord(worldIn, pos, iblockstate, itemstack);
                worldIn.playEvent((EntityPlayer)null, 1010, pos, Item.getIdFromItem(this));
                //worldIn.playSound(null,pos, SoundEventRegistryHandler.music_xjj, SoundCategory.NEUTRAL,1,1);
                itemstack.shrink(1);
                player.addStat(StatList.RECORD_PLAYED);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.PASS;
        }
    }
}
