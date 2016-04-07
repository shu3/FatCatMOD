package fatcat.ai;

import fatcat.entitiy.EntityFatCat;
import net.minecraft.entity.ai.EntityAIWander;

public class EntityAIFatCatWander extends EntityAIWander
{

    private EntityFatCat cat;

    public EntityAIFatCatWander(EntityFatCat cat, double speed)
    {
        super(cat, speed);
        this.cat = cat;
        this.setMutexBits(17);
    }

    @Override
    public boolean shouldExecute()
    {
        return this.cat.getFace() != EntityFatCat.Face.Sleep && this.cat.getRNG().nextInt(10) == 1 && super.shouldExecute();
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        this.cat.setAISit(true);
    }

    @Override
    public void startExecuting()
    {
        this.cat.setAISit(false);
        super.startExecuting();
    }

}
