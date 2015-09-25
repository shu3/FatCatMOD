package fatcat.ai;

import fatcat.EntityFatCat;
import fatcat.EntityFatCat.StatusChangeReason;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/* Try to sleep */
public class EntityAIFatCatSleep extends EntityAIBase {
	private EntityFatCat cat;
	private World world;
	public boolean tryWakeup = false;
	
	public EntityAIFatCatSleep(EntityFatCat cat) {
		this.cat = cat;
		this.world = cat.worldObj;
		this.setMutexBits(16);
	}

	@Override
	public boolean shouldExecute() {
//		System.out.println("EntityAIFatCatSleep: shouldExec="+(this.cat.getTiredness()));
		return (this.cat.getTiredness() >= EntityFatCat.TIREDNESS_MAX);
	}


    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
//		System.out.println("EntityAIFatCatSleep: continueExecuting="+(this.cat.getTiredness() > 0));
        return (this.cat.getTiredness() > 0 && !tryWakeup);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.cat.setAISit(true);
        this.cat.setFace(EntityFatCat.Face.Sleep);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.cat.setAISit(false);
        this.cat.setFace(EntityFatCat.Face.None);
        this.tryWakeup = false;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
    	this.cat.setTiredness(cat.getTiredness()-1, StatusChangeReason.Sleep);
    }
}
