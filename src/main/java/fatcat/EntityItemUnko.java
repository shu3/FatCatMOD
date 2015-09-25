package fatcat;

import java.util.Iterator;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityItemUnko extends EntityItem {

	public EntityItemUnko(World p_i1711_1_) {
		super(p_i1711_1_);
	}

	public EntityItemUnko(World p_i1709_1_, double p_i1709_2_,
			double p_i1709_4_, double p_i1709_6_) {
		super(p_i1709_1_, p_i1709_2_, p_i1709_4_, p_i1709_6_);
	}

	public EntityItemUnko(World p_i1710_1_, double p_i1710_2_,
			double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_) {
		super(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, p_i1710_8_);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (this.rand.nextFloat() < 0.1F) {
			FatCatMod.proxy.spawnParticle(EnumParticleTypes.SPELL_MOB_AMBIENT, this.posX + (rand.nextFloat()-0.5F), this.posY + 0.5D, this.posZ+(rand.nextFloat()-0.5F), 0.0D, 0.0D, 0.0D, 1);
		}
		
		// 近くにホカホカのウンコがあると友好度down・疲労度up
		if (this.ticksExisted % 20 == 0) {
			Iterator it = worldObj.getEntitiesWithinAABB(EntityFatCat.class, getEntityBoundingBox().expand(8.0D, 8.0D, 8.0D)).iterator();
			while(it.hasNext()) {
				EntityFatCat cat = (EntityFatCat) it.next();
				cat.setFriendship(cat.getFriendship()-10, EntityFatCat.StatusChangeReason.NearUnko);
				cat.setTiredness(cat.getTiredness()+10, EntityFatCat.StatusChangeReason.NearUnko);
			}
		}
	}

}
