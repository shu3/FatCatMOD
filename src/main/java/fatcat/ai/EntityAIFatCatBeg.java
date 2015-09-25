package fatcat.ai;

import fatcat.EntityFatCat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/* change interesting angle of neck */
public class EntityAIFatCatBeg extends EntityAIBase {
	private EntityFatCat cat;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int begTime;
    private int nextTime = 0;

	public EntityAIFatCatBeg(EntityFatCat cat, float distance) {
		this.cat = cat;
        this.worldObject = cat.worldObj;
        this.minPlayerDistance = distance;
	}

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.cat, (double)this.minPlayerDistance);
        if (nextTime > 0) {
        	nextTime--;
        	return false;
        }
        else if (this.thePlayer == null || this.cat.isInSleep() || this.cat.getPose() != EntityFatCat.Pose.None) {
        	return false;
        }
//        System.out.println("EntityAIFatCatBeg: shouldExecute(): food="+this.hasPlayerGotFoodInHand(this.thePlayer));
        return this.hasPlayerGotFoodInHand(this.thePlayer);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if (this.thePlayer.isEntityAlive()) {
    		if ((this.cat.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance))) {
    			return false;
    		}
    		else {
    			return this.begTime > 0 && this.hasPlayerGotFoodInHand(this.thePlayer);
    		}
    	}
    	else {
    		return false;
    	}
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.cat.setPose(EntityFatCat.Pose.Beg);
        this.begTime = 40 + this.cat.getRNG().nextInt(40);
        this.nextTime = 40 + this.cat.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.cat.setPose(EntityFatCat.Pose.None);
        this.thePlayer = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.cat.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.cat.getVerticalFaceSpeed());
        --this.begTime;
    }

    /**
     * Gets if the Player has some food in the hand.
     */
    private boolean hasPlayerGotFoodInHand(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack == null) {
        	return false;
        }
        return itemstack.getItem().getCreativeTab().getTabLabel().equals("food");
    }
}
