/**
 * 
 */
package fatcat.ai;

import fatcat.EntityFatCat;
import fatcat.FatCatMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIEatBlock extends EntityAIBase {
	private EntityFatCat cat;
	private World world;
	private float frequency;
	private Vec3 closestPos;
	private Block targetBlock;
	private int giveuptime;

	public EntityAIEatBlock(EntityFatCat cat) {
		this.cat = cat;
		this.world = cat.worldObj;
		this.frequency = 0.25f;
        this.setMutexBits(11);
//		this.frequency = 0.01F;
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.cat.getRNG().nextFloat() > frequency)  {
			return false;
		}
		else if (this.cat.isInSleep() || this.cat.getLeashed() || !this.cat.isHungry()) {
			return false;
		}
		else if (closestPos != null) {
			return false;
		}
		else {
			findBlock();

			return (this.closestPos != null);
		}
	}

	private void findBlock() {
		Block block;
		double closestPosDistance = 100.0D;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Vec3 pos = new Vec3(MathHelper.floor_double(cat.posX+x-8), MathHelper.floor_double(cat.posY+y-1), MathHelper.floor_double(cat.posZ+z-8));
					double d = cat.getDistance(pos.xCoord, pos.yCoord, pos.zCoord);
					if (checkBlock(pos) && (d > 1.0D) && (d < closestPosDistance)) {
						FatCatMod.proxy.log(this.world, "EntityAIEatBlock: found %s", pos.toString());
						this.closestPos = pos;
						closestPosDistance = d;
					}
				}
			}
			if (closestPosDistance < 100.0D) {
				return;
			}
		}
	}


    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (this.giveuptime > 0 && checkBlock(closestPos)) {
        	return true;
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
        this.giveuptime = 50;
        this.cat.setAISit(false);
        this.cat.setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.cat.setAISit(true);
        this.closestPos = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.cat.getLookHelper().setLookPosition(this.closestPos.xCoord+0.5, this.closestPos.yCoord, this.closestPos.zCoord+0.5, 10.0F, (float)this.cat.getVerticalFaceSpeed());
        if ((this.giveuptime%10) == 0) {
        	this.cat.getNavigator().tryMoveToXYZ(this.closestPos.xCoord+0.5, this.closestPos.yCoord+1, this.closestPos.zCoord+0.5, 0.5f);
        }
        if (cat.getDistanceSqToCenter(new BlockPos(closestPos)) < 1.0D) {
        	this.cat.eatBlockBounus(world.getBlockState(new BlockPos(closestPos.xCoord, closestPos.yCoord, closestPos.zCoord)).getBlock());
        	this.world.destroyBlock(new BlockPos(closestPos.xCoord, closestPos.yCoord, closestPos.zCoord), false);
        	this.giveuptime = 0;
        }
        --this.giveuptime;
    }
    
    private boolean checkBlock(Vec3 pos) throws RuntimeException {
    	BlockPos blockPos = new BlockPos(pos.xCoord, pos.yCoord, pos.zCoord);
    	Block block = world.getBlockState(blockPos).getBlock();
    	if (block == null || pos == null) {
    		return false;
    	}
    	if (!world.isAirBlock(blockPos.add(0, 1, 0))) {
    		return false;
    	}
    	
    	if (block == Blocks.potatoes || block == Blocks.tallgrass || block == Blocks.brown_mushroom_block ||
    		block == Blocks.red_mushroom_block || block == Blocks.carrots || block == Blocks.wheat ||
    		block == Blocks.reeds || block == Blocks.melon_block) {
    		return true;
    	}
    	
		return false;
    }

}
