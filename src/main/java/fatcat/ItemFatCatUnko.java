package fatcat;

import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class ItemFatCatUnko extends Item {

	public ItemFatCatUnko() {
		super();
        this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (applyUnko(stack, worldIn, pos, playerIn))
        {
            if (!worldIn.isRemote)
            {
                worldIn.playAuxSFX(2005, pos, 0);
            }

            return true;
        }
        return false;
	}

	private boolean applyUnko(ItemStack stack, World worldIn, BlockPos pos, EntityPlayer player) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block instanceof IGrowable)
        {
            IGrowable igrowable = (IGrowable)block;

            if (igrowable.isStillGrowing(worldIn, pos, iblockstate, worldIn.isRemote))
            {
                if (!worldIn.isRemote)
                {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, iblockstate))
                    {
                        igrowable.grow(worldIn, worldIn.rand, pos, iblockstate);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }

        return false;
	}
}
