
package cn.fhyjs.jntm.block;
//包声明

import cn.fhyjs.jntm.Jntm;

import cn.fhyjs.jntm.registry.SoundEventRegistryHandler;
import ibxm.Player;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;
//导入主类

public class cookedcxk extends Block{
    //private static IntegerProperty STATE = IntegerProperty;
    public static int statei = -1;
    public static final AxisAlignedBB pzx0 = new AxisAlignedBB(0, 0, 0, 1, 0.25D, 1);
    public static final AxisAlignedBB pzx1 = new AxisAlignedBB(0, 0, 0, 0.75D, 0.25D, 1);
    public static final AxisAlignedBB pzx2 = new AxisAlignedBB(0, 0, 0, 0.4375D, 0.25D, 1);
    public static final AxisAlignedBB pzx3 = new AxisAlignedBB(0, 0, 0, 0.125D, 0.25D, 1);
    public static final PropertyInteger STATE_BLOCK_SWITCH = PropertyInteger.create("state_switch",0,3);
    public cookedcxk()
    {
        this(Material.GROUND);
        //初始化
        this.setUnlocalizedName(Jntm.MODID+"cookedcxk");
        //设置UnlocalizedName
        this.setRegistryName("cookedcxk");
        //设置物品ID
        this.setHarvestLevel("pickaxe",0);
        //需要的工具{稿子：pickaxe，剑：sword......}和挖掘等级，这个可以任意选取，钻石镐为3
        this.setHardness(2F);
        //设置硬度，黑曜石是50
        this.setCreativeTab(jntm_Group);
        this.setLightLevel((float)5/(float)16);
        this.setLightOpacity(7);
    }
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        // TODO Auto-generated method stub
        tooltip.add(new TextComponentTranslation("tooltip.jntmcookedcxk.name").getFormattedText());
        super.addInformation(stack, player, tooltip, advanced);
    }
    private cookedcxk(Material material)
    {
        super(material);
        //初始化
        setDefaultState(this.blockState.getBaseState().withProperty(STATE_BLOCK_SWITCH,0));


    }
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this,new IProperty[] {STATE_BLOCK_SWITCH});
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        int axel = state.getValue(STATE_BLOCK_SWITCH);
        return axel;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STATE_BLOCK_SWITCH, meta);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        statei = state.getValue(STATE_BLOCK_SWITCH);
        if(statei==0){
            return pzx0;
        }
        if(statei==1){
            return pzx1;
        }
        if(statei==2){
            return pzx2;
        }
        if(statei==3){
            return pzx3;
        }
        return pzx0;
    }
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }



    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        world.playSound(null,pos, SoundEventRegistryHandler.xainga,SoundCategory.NEUTRAL,1,1);
        if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT) {
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, false, pos.getX(), pos.getY(), pos.getZ(), 1, 1, 1, 1, 1);
        }
        FoodStats food = player.getFoodStats();
        food.setFoodLevel(food.getFoodLevel() + 5);
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 10000, 1));
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 1000, 1));
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(12), 2000, 1));

        if (state.getValue(STATE_BLOCK_SWITCH)+1>3){
            world.destroyBlock(pos,false);
            return true;
        }
        world.setBlockState(pos, getStateFromMeta(state.getValue(STATE_BLOCK_SWITCH)+1));
        if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT) {
            player.sendStatusMessage(new TextComponentString(statei + ""), true);
        }
        return true;

    }
}