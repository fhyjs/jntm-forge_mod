package cn.fhyjs.jntm.registry;

import cn.fhyjs.jntm.entity.JGPDanmaku;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DispenserBehaviorRegistryHandler {
    public static void run() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistryHandler.jgpdtex,new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                return new JGPDanmaku(worldIn,position.getX(),position.getY(),position.getZ());
            }
        });
    }
}
