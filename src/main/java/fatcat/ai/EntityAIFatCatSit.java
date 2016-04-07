package fatcat.ai;

import fatcat.FatCatMod;
import fatcat.entitiy.EntityFatCat;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIFatCatSit extends EntityAIBase
{

    private EntityFatCat cat;
    private World world;
    private float frequency;
    private Vec3 closestPlatePos = null;
    private int giveUpTime;
    private FindType findType = FindType.Plate;

    public EntityAIFatCatSit(EntityFatCat cat)
    {
        this.cat = cat;
        this.world = cat.worldObj;
        this.frequency = 0.003f;
//		this.frequency = 0.1f;
        this.setMutexBits(12);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.cat.getRNG().nextFloat() > frequency)
        {
            return false;
        }
        else if (this.cat.getFace() == EntityFatCat.Face.Sleep || this.cat.getLeashed())
        {
            return false;
        }
        else if (closestPlatePos != null)
        {
            return false;
        }
        else
        {
            //System.out.println("EntityAIFatCatSit: findSitPlace");
            findType = FindType.values()[cat.getRNG().nextInt(FindType.values().length)];
            findSitSpot();
            return (this.closestPlatePos != null);
        }
    }

    private void findSitSpot()
    {
        double closestPosDistance = 100.0D;
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    Vec3 pos = new Vec3(MathHelper.floor_double(cat.posX + x - 8), MathHelper.floor_double(cat.posY + y - 1), MathHelper.floor_double(cat.posZ + z - 8));
                    double d = cat.getDistance(pos.xCoord, pos.yCoord, pos.zCoord);
                    if (checkBlock(findType, pos) && (d > 1.0D) && (d < closestPosDistance))
                    {
                        FatCatMod.proxy.log(cat.worldObj, "EntityAIFatCatSit: found=<%s>", pos);
                        this.closestPlatePos = pos;
                        closestPosDistance = d;
                    }
                }
            }
            if (closestPosDistance < 100.0D)
            {
                return;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !(cat.getDistanceSqToCenter(new BlockPos(closestPlatePos).up()) < 1.0D) && this.giveUpTime > 0 && checkBlock(findType, closestPlatePos);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.giveUpTime = 200;
        this.cat.setAISit(false);
        this.cat.setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.cat.setAISit(true);
        this.closestPlatePos = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.cat.getLookHelper().setLookPosition(this.closestPlatePos.xCoord + 0.5, this.closestPlatePos.yCoord, this.closestPlatePos.zCoord + 0.5, 10.0F, (float) this.cat.getVerticalFaceSpeed());
        if ((this.giveUpTime % 10) == 0)
        {
            this.cat.getNavigator().tryMoveToXYZ(this.closestPlatePos.xCoord + 0.5, this.closestPlatePos.yCoord + 1, this.closestPlatePos.zCoord + 0.5, 0.3f);
        }
        --this.giveUpTime;
    }

    private boolean checkBlock(FindType type, Vec3 pos) throws RuntimeException
    {
        BlockPos blockPos = new BlockPos(pos.xCoord, pos.yCoord, pos.zCoord);
        Block block = world.getBlockState(blockPos).getBlock();
        if (block == null)
        {
            return false;
        }
        if (!world.isAirBlock(blockPos.add(0, 1, 0)))
        {
            return false;
        }

        if (type == FindType.Plate)
        {
            if (block == Blocks.wooden_pressure_plate)
            {
                return true;
            }
        }
        else if (type == FindType.Bed)
        {
            if (block == Blocks.bed && block.isBedFoot(cat.worldObj, blockPos))
            {
                return true;
            }
        }
        else if (type == FindType.Furnace)
        {
            if (block == Blocks.lit_furnace)
            {
                return true;
            }
        }
        else
        {
            throw new RuntimeException("Unexpected find type = " + type);
        }
        return false;
    }

    private enum FindType {Plate, Bed, Furnace}

}
