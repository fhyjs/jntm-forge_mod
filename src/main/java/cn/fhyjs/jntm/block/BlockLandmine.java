package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.network.JntmGuiHandler;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

import java.util.Random;

import static cn.fhyjs.jntm.ItemGroup.jntmGroup.jntm_Group;

public class BlockLandmine extends BlockTileEntity<TELandmine> {
    public final static PropertyBool NOTUSE = PropertyBool.create("l");
    public BlockLandmine() {
        super(Material.GROUND);
        //初始化
        this.setUnlocalizedName(Jntm.MODID+".Landmine");
        //设置UnlocalizedName
        this.setRegistryName("Landmine");
        //设置物品ID
        this.setHarvestLevel("pickaxe",0);
        //需要的工具{稿子：pickaxe，剑：sword......}和挖掘等级，这个可以任意选取，钻石镐为3
        this.setHardness(25);
        //设置硬度，黑曜石是50
        this.setCreativeTab(jntm_Group);

        this.setDefaultState(this.blockState.getBaseState().withProperty(NOTUSE, false));
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, NOTUSE);
    }
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(NOTUSE, false);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(NOTUSE)?1:0;
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(NOTUSE, meta==1);
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (getTileEntity(source,pos)!=null) {
            return new AxisAlignedBB(0,0,0,1, getTileEntity(source, pos).getThickness(),1);
        }
        return new AxisAlignedBB(0,0,0,1,1,1);
    }
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this),1);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt==null) nbt = new NBTTagCompound();
        nbt.setTag("BlockEntityTag",worldIn.getTileEntity(pos).serializeNBT());
        NBTTagList list = new NBTTagList();
        list.appendTag(new NBTTagString(I18n.translateToLocal("tip.configured")));
        NBTTagCompound display = new NBTTagCompound();
        display.setTag("Lore",list);
        nbt.setTag("display",display);
        stack.setTagCompound(nbt);
        worldIn.spawnEntity(new EntityItem(worldIn,pos.getX(),pos.getY(),pos.getZ(),stack));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        //Item item = super.getItemDropped(state, rand, fortune);
        return null;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        check(world,pos);
    }
    public void check(IBlockAccess world,BlockPos pos){
        if (world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()==this){
            FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),"say "+ I18n.translateToLocalFormatted("tip.jntm.cannotput.up",
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock().getLocalizedName()
            ));
            world.getTileEntity(pos).getWorld().destroyBlock(pos,false);
        }
        if (world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()== Blocks.AIR){
            world.getTileEntity(pos).getWorld().destroyBlock(pos,false);
        }
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()==this){
            FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),"say "+ I18n.translateToLocalFormatted("tip.jntm.cannotput.up",
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    worldIn.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock().getLocalizedName()
            ));
            worldIn.destroyBlock(pos,false);
        }
    }

    @Override
    public Class<TELandmine> getTileEntityClass() {
        return TELandmine.class;
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
    public TELandmine createTileEntity(World world, IBlockState state) {
        return new TELandmine();
    }
}
