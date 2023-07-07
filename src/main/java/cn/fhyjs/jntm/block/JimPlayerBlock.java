package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.common.pstest;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import cn.fhyjs.jntm.network.Opt_Ply_Message;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

import java.io.File;
import java.util.Objects;
import java.util.Random;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class JimPlayerBlock extends BlockTileEntity<TEJimPlayer>{
    public JimPlayerBlock() {
        super(Material.WOOD);
        this.setRegistryName("jimplayer");
        this.setUnlocalizedName(Jntm.MODID + "jimplayer");
        this.setCreativeTab(jntm_Group);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setHarvestLevel("pickaxe",0);
        this.setHardness(2F);
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(FACING).add(
                Properties.AnimationProperty).add(Properties.StaticProperty).build();
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
        return i;
    }
    private boolean powered;
    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn,pos,state);
        worldIn.updateComparatorOutputLevel(pos, this);
        if(!worldIn.isRemote&&CommonProxy.jimplayers.get(pos)!=null){
            CommonProxy.jimplayers.get(pos).stop();
            CommonProxy.jimplayers.remove(pos);
        }
    }
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
            if (CommonProxy.jimplayers.get(pos)!=null)
                if (CommonProxy.jimplayers.get(pos).isAlive())
                return 15;
        return 0;
    }
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);
        if (!world.isRemote)
        {
            powered=world.isBlockPowered(pos);
        }
    }
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            powered=worldIn.isBlockPowered(pos);
            if (powered && !((TEJimPlayer)worldIn.getTileEntity(pos)).f1){
                if (CommonProxy.jimplayers.get(pos)!=null)
                    if (CommonProxy.jimplayers.get(pos).isAlive()) {
                        CommonProxy.jimplayers.get(pos).stop();
                        ((TEJimPlayer)worldIn.getTileEntity(pos)).onCloseInventory();
                    }
                    else {
                        CommonProxy.jimplayers.put(pos, new pstest(null, worldIn, pos, new File((getrunpath("jims/") + getTileEntity(worldIn, pos).count).replaceAll("\n", " "))));
                        CommonProxy.jimplayers.get(pos).start();
                        ((TEJimPlayer)worldIn.getTileEntity(pos)).onOpenInventory();
                    }
                else {
                    CommonProxy.jimplayers.put(pos, new pstest(null, worldIn, pos, new File((getrunpath("jims/") + getTileEntity(worldIn, pos).count).replaceAll("\n", " "))));
                    CommonProxy.jimplayers.get(pos).start();
                    ((TEJimPlayer)worldIn.getTileEntity(pos)).onOpenInventory();
                }
                ((TEJimPlayer)worldIn.getTileEntity(pos)).f1=true;
                ((TEJimPlayer)worldIn.getTileEntity(pos)).markDirty();
            }
            if (!powered) {
                ((TEJimPlayer)worldIn.getTileEntity(pos)).f1=false;
                ((TEJimPlayer)worldIn.getTileEntity(pos)).markDirty();
            }

        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {

    }
    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        if (!world.isRemote)
            return true;
        else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null)
                return tile.receiveClientEvent(id, param);
            else
                return false;
        }
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {


        if (!world.isRemote) {
            player.openGui(Jntm.instance, JntmGuiHandler.GUIs.RSMPlayer.getId(), world, (int) pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }
    @Nullable
    @Override
    public TEJimPlayer createTileEntity(World world, IBlockState state) {
        return new TEJimPlayer();
    }
    public String getrunpath(String sp){
        String MP;
        StringBuilder tmp;
        String[] temp;
        temp = Loader.instance().getConfigDir().toURI().toString().split("/");
        tmp = new StringBuilder();
        for (int i = 1; i < temp.length - 1; i++) {
            tmp.append(temp[i]).append("/");
        }
        tmp.append(sp).append("/");
        MP = tmp.toString();
        return MP;
    }
    @Override
    public Class<TEJimPlayer> getTileEntityClass() {
        return TEJimPlayer.class;
    }
}
