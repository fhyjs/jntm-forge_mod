package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.renderer.RenderCxkImageTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class Cxkimage extends BlockTileEntity<TileEntityCxkImage> {
    public Cxkimage() {
        super(Material.WOOD);
        this.setRegistryName("cxkimages");
        this.setUnlocalizedName(Jntm.MODID + "cxkimages");
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
            TileEntityCxkImage tile = getTileEntity(world, pos);
            Jntm.logger.error("url: " + tile.getCount());
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.SetCxkimage.getId(), world, (int) pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn,pos,state);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            RenderCxkImageTileEntity.rlm.remove((TileEntityCxkImage) worldIn.getTileEntity(pos));
            RenderCxkImageTileEntity.mapo.remove((TileEntityCxkImage) worldIn.getTileEntity(pos));
            RenderCxkImageTileEntity.map.remove((TileEntityCxkImage) worldIn.getTileEntity(pos));
        }
    }
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return new AxisAlignedBB(0, 0, 0, 1, 1, .1);
            case SOUTH:
                return new AxisAlignedBB(1, 1, 1, 0, 0, .9);
            case WEST:
                return new AxisAlignedBB(.1, 1, 1, 0, 0, 0);
            case EAST:
            default:
                return new AxisAlignedBB(1, 1, 1, .9, 0, 0);
        }
    }
    @Override
    public Class<TileEntityCxkImage> getTileEntityClass() {
        return TileEntityCxkImage.class;
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
    public TileEntityCxkImage createTileEntity(World world, IBlockState state) {
        return new TileEntityCxkImage();
    }

}