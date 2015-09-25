package fatcat;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFeatherToy extends Item {
	public static int MAX_DAMAGE = 64;
	
	public ItemFeatherToy() {
		super();
        this.setMaxDamage(MAX_DAMAGE);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
	}

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	player.swingItem();
        return stack;
    }

}
