package fatcat.gui;

import fatcat.entitiy.EntityFatCat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.List;

public class GuiStatusHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
//		System.out.println("fatcat.GuiStatusHandler: getServerGuiElement x="+x+",y="+y+",z="+z);
        List list = world.getEntitiesWithinAABB(EntityFatCat.class, AxisAlignedBB.fromBounds(x, y, z, x, y, z).expand(1.0F, 1.0F, 1.0F));
        if (!list.isEmpty())
        {
            EntityFatCat cat = (EntityFatCat) list.get(0);
            return new ContainerStatus(player, cat);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
//		System.out.println("fatcat.GuiStatusHandler: getClientGuiElement() x="+x+",y="+y+",z="+z);
        List list = world.getEntitiesWithinAABB(EntityFatCat.class, AxisAlignedBB.fromBounds(x, y, z, x, y, z).expand(1.0F, 1.0F, 1.0F));
        if (!list.isEmpty())
        {
            EntityFatCat cat = (EntityFatCat) list.get(0);
            return new GuiStatus(player, cat);
        }
        return null;
    }

}
