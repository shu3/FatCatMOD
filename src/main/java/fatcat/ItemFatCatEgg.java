package fatcat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemFatCatEgg extends Item {
	public ItemFatCatEgg() {
		super();
        this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            pos = pos.offset(side);

            EntityFatCat entity = new EntityFatCat(worldIn);
            entity.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
            // set OwnerID
            entity.func_152115_b(playerIn.getUniqueID().toString());
            worldIn.spawnEntityInWorld(entity);

            if (entity != null)
            {
                if (!playerIn.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return true;
        }
    }
}
