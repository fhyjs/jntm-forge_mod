package cn.fhyjs.jntm.block;

import cn.fhyjs.jntm.Jntm;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
        this.setHardness(50F);
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
