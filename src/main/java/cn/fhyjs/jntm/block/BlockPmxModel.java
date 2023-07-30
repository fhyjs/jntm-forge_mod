package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.gui.CheckMDR;
import cn.fhyjs.jntm.gui.JntmGuiFactory;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class BlockPmxModel extends BlockTileEntity<TEPmxModel> {
    public BlockPmxModel() {
        super(Material.WOOD);
        //初始化
        this.setUnlocalizedName(Jntm.MODID+".BlockPmxModel");
        //设置UnlocalizedName
        this.setRegistryName("BlockPmxModel");
        //设置物品ID
        this.setHarvestLevel("pickaxe",0);
        //需要的工具{稿子：pickaxe，剑：sword......}和挖掘等级，这个可以任意选取，钻石镐为3
        this.setHardness(25);
        //设置硬度，黑曜石是50
        this.setCreativeTab(jntm_Group);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        return i;
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    @Override
    public void addInformation(ItemStack stack, @org.jetbrains.annotations.Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1,0.2,1);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;
        if (!CheckMDR.isInstallMMDR()){
            playerIn.openGui(Jntm.INSTANCE, JntmGuiHandler.GUIs.CheckMMDR.getId(), worldIn,pos.getX(),pos.getY(),pos.getZ());
        }else{
            playerIn.openGui(Jntm.INSTANCE, JntmGuiHandler.GUIs.SetPmxModel.getId(), worldIn,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }

    @Override
    public Class<TEPmxModel> getTileEntityClass() {
        return TEPmxModel.class;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @Nullable
    @Override
    public TEPmxModel createTileEntity(World world, IBlockState state) {
        return new TEPmxModel();
    }
}
