package fatcat.gui;

import fatcat.EntityFatCat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerStatus extends Container {
	private EntityFatCat cat;
	private EntityPlayer player;

	public ContainerStatus(EntityPlayer player, EntityFatCat cat) {
		this.player = player;
		this.cat = cat;
	}

	/**
	 * This should always return true, since custom inventory can be accessed from anywhere
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
