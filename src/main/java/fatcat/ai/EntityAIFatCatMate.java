package fatcat.ai;

import fatcat.entitiy.EntityFatCat;
import fatcat.entitiy.EntityFatCat.StatusChangeReason;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/* 恋愛度が高ければ子供を作る */
public class EntityAIFatCatMate extends EntityAIBase
{

    private static final int CAT_MIN_WEIGHT = 4000;
    private EntityFatCat cat;
    private EntityFatCat mate;
    private World worldObj;
    private int matingTimeout;
    private int tick;

    public EntityAIFatCatMate(EntityFatCat cat)
    {
        this.cat = cat;
        this.worldObj = cat.worldObj;
        this.setMutexBits(15);
    }

    @Override
    public boolean shouldExecute()
    {
//		System.out.println("EntityAIFatCatMate(shouldExecute): cs="+checkSufficientMating(cat));
        boolean exec = true;
        if (cat.getRNG().nextInt(500) != 0)
        {
            exec = false;
        }
        else if (cat.isMating)
        {
            exec = false;
        }
        else if (!checkSufficientMating(cat))
        {
            exec = false;
        }

        if (exec || cat.tryMating)
        {
            EntityFatCat entity = (EntityFatCat) this.worldObj.findNearestEntityWithinAABB(EntityFatCat.class, cat.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), this.cat);
            exec = checkSufficientMating(mate);
            this.mate = entity;
//       	System.out.println("EntityAIFatCatMate(shouldExecute): exec="+exec+",");
        }
        return exec;
    }

    @Override
    public void startExecuting()
    {
        this.matingTimeout = 300;
        this.tick = 0;
        this.cat.isMating = true;
        this.cat.setAISit(false);
        if (!this.mate.isMating)
        {
            this.mate.tryMating = true;
        }
    }

    @Override
    public void resetTask()
    {
        this.cat.isMating = false;
        this.mate.isMating = false;
        this.cat.tryMating = false;
        this.cat.setAISit(true);
        this.mate = null;
    }

    @Override
    public boolean continueExecuting()
    {
        return this.matingTimeout >= 0 && cat.isMating && checkSufficientMating(cat) && checkSufficientMating(mate);
    }

    @Override
    public void updateTask()
    {
        --this.matingTimeout;
        this.cat.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);

        if (tick % 50 == 0)
        {
            cat.generateRandomParticles(EnumParticleTypes.HEART);
        }
//    	System.out.println("EntityAIFatCatMate(updateTask): tick="+tick);

        if (this.cat.getDistanceSqToEntity(this.mate) > 2.25D)
        {
            this.cat.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
        }
        else if (this.matingTimeout == 0 && this.mate.isMating)
        {
            this.giveBirth();
        }
        this.tick++;
    }

    private boolean checkSufficientMating(EntityFatCat cat)
    {
//		System.out.println("EntityAIFatCatMate(checkSufficientMating): weight="+cat.getWeight()+",love="+cat.getLove());
        return (cat != null) && (!cat.isChild()) && (cat.getLove() >= EntityFatCat.LOVE_MAX);
    }

    private void giveBirth()
    {
//    	System.out.println("EntityAIFatCatMate(shouldExecute): getBirth");
        EntityFatCat child = this.cat.createChild(this.mate);
        child.setLocationAndAngles(this.cat.posX, this.cat.posY, this.cat.posZ, 0.0F, 0.0F);
        worldObj.spawnEntityInWorld(child);
        cat.setLove(0, StatusChangeReason.Spawn);
        mate.setLove(0, StatusChangeReason.Spawn);
    }

}
