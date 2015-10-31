package fatcat.ai;

import java.util.List;

import fatcat.EntityFatCat;
import fatcat.EntityItemUnko;
import fatcat.FatCatMod;
import fatcat.ItemFatCatUnko;
import fatcat.ItemFurball;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemReed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;

// Find near food entity and eat it.
public class EntityAIEatEntityItem extends EntityAIBase {
	private EntityFatCat cat;
	private float speed;
	private int giveuplimit;
	private float frequency;
	private EntityItem closestItem;
	private int giveuptime;
	private static final float NONFOOD_EAT_FREQ = 0.01F;
	
	public EntityAIEatEntityItem(EntityFatCat cat, float frequency, float speed, int giveuplimit) {
		this.cat = cat;
		this.speed = speed;
		this.giveuplimit = giveuplimit;
		this.frequency = frequency;
	}

	@Override
	public boolean shouldExecute() {
		if (this.cat.getLeashed() || !this.cat.isEatable()) {
			return false;
		}
		else if (this.cat.getRNG().nextFloat() > frequency)  {
			return false;
		}
		else {
			this.closestItem = (EntityItem)this.cat.worldObj.findNearestEntityWithinAABB(EntityItem.class, this.cat.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), this.cat);
			boolean exec = (this.closestItem != null && isFindableItem(this.closestItem.getEntityItem().getItem()));
//			System.out.println("EntityAIEatEntityItem: exec="+exec+",item="+closestItem);

			boolean res = false;
			if (this.closestItem != null)  {
				Item food = this.closestItem.getEntityItem().getItem();
				res = isFindableItem(food);
				// 食べ物以外は餓死寸前の状態だと食べてしまう
				if (res && !cat.isFoodItem(food)) {
					FatCatMod.proxy.log(this.cat.worldObj, "EntityAIEatEntityItem: shouldExecute() -> non food(%s), starved(%s)", food.toString(), cat.isStarved());
					res = this.cat.isStarved();
				}
			}
			return res;
		}
	}

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (this.closestItem.isEntityAlive() && this.giveuptime > 0) {
        	return true;
        }
        else {
            this.cat.setAISit(true);
            this.cat.setSprinting(false);
        	return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.giveuptime = this.giveuplimit;
        this.cat.setAISit(false);
        this.cat.setSitting(false);
        this.cat.setSprinting(true);
        this.cat.cancelPose();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.closestItem = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.cat.getLookHelper().setLookPosition(this.closestItem.posX, this.closestItem.posY + (double)this.closestItem.getEyeHeight(), this.closestItem.posZ, 10.0F, (float)this.cat.getVerticalFaceSpeed());
        if ((this.giveuptime%10) == 0) {
        	boolean tried = this.cat.getNavigator().tryMoveToEntityLiving(this.closestItem, speed);
        }
        if (isCollideEntityItem(this.cat, this.closestItem)) {
        	this.eatEntityItem(this.closestItem);
        	this.cat.eatEntityBounus(this.closestItem);
        }
        --this.giveuptime;
    }
    
    /**
     * @param food
     */
    private void eatEntityItem(EntityItem food) {
    	FatCatMod.proxy.spawnParticle(
    			EnumParticleTypes.ITEM_CRACK, food.posX, food.posY+0.5, food.posZ,
    			this.cat.getRNG().nextGaussian() * 0.15D, this.cat.getRNG().nextDouble() * 0.2D, this.cat.getRNG().nextGaussian() * 0.15D, 10,
    			new int[] {Item.getIdFromItem(food.getEntityItem().getItem())});
    	cat.worldObj.playSoundEffect(cat.posX+0.5D, cat.posY+0.5D, cat.posZ+0.5D, "random.eat", 1.0F, 1.0F);
    	// もしPlayerが取っても加算されないようにする
    	if (food.getEntityItem() != null) {
    		food.getEntityItem().stackSize = 0;
    	}
    	food.setDead();
    }
    
    private boolean isFindableItem(Item food) {
    	return (food != null && !(food instanceof ItemFatCatUnko) &&
    			!(food instanceof ItemFurball) && !(food instanceof ItemLead) &&
    			!(food instanceof ItemNameTag));
    }
    
    private boolean isCollideEntityItem(EntityFatCat cat, Entity item) {
    	AxisAlignedBB axisalignedbb = cat.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D);
    	List list = cat.worldObj.getEntitiesWithinAABBExcludingEntity(cat, axisalignedbb);
    	return list.contains(item);
    }
}
