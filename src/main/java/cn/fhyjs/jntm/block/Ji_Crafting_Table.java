package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.renderer.RenderCxkImageTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.swing.text.JTextComponent;

import java.util.Objects;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Ji_Crafting_Table extends BlockTileEntity<TileEntityJiCrafting> {
    public Ji_Crafting_Table() {
        super(Material.WOOD,new TileEntityJiCrafting());
        this.setRegistryName("jicrafting");
        this.setUnlocalizedName(Jntm.MODID + "jicrafting");
        this.setCreativeTab(jntm_Group);
        this.setHarvestLevel("pickaxe",0);
        this.setHardness(2F);
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
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        //getTileEntity(worldIn,pos).count="not_set";
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.Ji_Crafting.getId(), world, (int) pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
    }
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        ItemStackHandler a = null;
        if (tileentity != null) {
            a = ((TileEntityJiCrafting)tileentity).getFilterList();
        }
        if (a != null) {
            for (int i = 0;i<a.getSlots();i++){
                worldIn.spawnEntity(new EntityItem(worldIn,pos.getX(),pos.getY(),pos.getZ(),a.getStackInSlot(i)));
            }
        }
        worldIn.updateComparatorOutputLevel(pos, this);

        super.breakBlock(worldIn, pos, state);
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
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    @Override
    public Class<TileEntityJiCrafting> getTileEntityClass() {
        return TileEntityJiCrafting.class;
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
    public TileEntityJiCrafting createTileEntity(World world, IBlockState state) {
        return new TileEntityJiCrafting();
    }

}