package fatcat.ai;

import fatcat.entitiy.EntityFatCat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;

/* Attack to unfriendly owner sometimes */
public class EntityAIAttackUnfriendlyOwner extends EntityAIBase
{

    private EntityFatCat cat;
    private float frequency;
    private int tick;
    private int giveUp = 100;

    public EntityAIAttackUnfriendlyOwner(EntityFatCat cat)
    {
        this.cat = cat;
        this.frequency = 0.005F;
//		this.frequency = 0.1F;
        this.setMutexBits(10);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.cat.getRNG().nextFloat() > frequency)
        {
            return false;
        }
        else if (this.cat.isInSleep() || this.cat.getLeashed() || this.cat.getOwner() == null)
        {
            return false;
        }
        else if (this.cat.getFriendship() >= (EntityFatCat.FRIENDSHIP_MAX * 0.2))
        {
            return false;
        }
        return true;
    }

    @Override
    public void startExecuting()
    {
        this.cat.setAISit(false);
        this.cat.setSitting(false);
        tick = 0;
    }

    @Override
    public void resetTask()
    {
        this.cat.setAISit(true);
    }

    @Override
    public boolean continueExecuting()
    {
        return (!cat.getOwner().isDead && tick < giveUp);
    }

    @Override
    public void updateTask()
    {
        EntityLivingBase owner = (EntityLivingBase) cat.getOwner();
        if (tick % 10 == 0)
        {
            if (cat.getNavigator().tryMoveToXYZ(owner.posX, owner.posY, owner.posZ, 0.5F))
            {
                if (tick % 50 == 0)
                {
                    cat.generateRandomParticles(EnumParticleTypes.VILLAGER_ANGRY);
                }
            }
        }
        if (this.cat.getDistanceSqToEntity(owner) < 1.0D)
        {
            this.cat.attackEntityAsMob(owner);
            tick = giveUp + 1;
        }
        tick++;
    }

}
