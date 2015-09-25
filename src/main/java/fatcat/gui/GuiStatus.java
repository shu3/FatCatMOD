package fatcat.gui;

import org.lwjgl.opengl.GL11;

import fatcat.EntityFatCat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.ResourceLocation;

public class GuiStatus extends GuiContainer {

    private static final ResourceLocation textures = new ResourceLocation("fatcat:textures/gui/gui_fatcat_status.png");
    private EntityFatCat cat;
    private EntityPlayer player;

	public GuiStatus(EntityPlayer player, EntityFatCat cat) {
		super(new ContainerStatus(player, cat));
		this.cat = cat;
		this.player = player;
		this.xSize = 200;
		this.ySize = 127;
        this.allowUserInput = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(textures);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        // weight
		drawIconBar(cat.getWeight(), EntityFatCat.WEIGHT_STATUS_MAX, 10, guiLeft + 70, guiTop + 22, xSize, 3*8, xSize+8, 3*8);
		// health
		drawIconBar(cat.getHealth(), cat.getMaxHealth(), 5, guiLeft + 70, guiTop + 46, xSize, 1*8, xSize+8, 1*8);
		// hunger
		drawIconBar(cat.getHunger(), EntityFatCat.HUNGER_MAX, 5, guiLeft + 130, guiTop + 46, xSize, 2*8, xSize+8, 2*8);
		// bladder
		drawIconBar(cat.getBladder(), EntityFatCat.BLADDER_MAX, 5, guiLeft + 70, guiTop + 68, xSize, 0*8, xSize+8, 0*8);
		// tiredness
		drawIconBar(cat.getTiredness(), EntityFatCat.TIREDNESS_MAX, 5, guiLeft + 130, guiTop + 68, xSize, 8*4, xSize+8, 8*4);
		// friendship
		drawIconBar(cat.getFriendship(), EntityFatCat.FRIENDSHIP_MAX, 5, guiLeft + 70, guiTop + 90, xSize, 8*5, xSize+8, 8*5);
		// loveness
		drawIconBar(cat.getLoveness(), EntityFatCat.FRIENDSHIP_MAX, 5, guiLeft + 130, guiTop + 90, xSize, 8*6, xSize+8, 8*6);
		
		
		// !! change scale in the following method, so we should put this at last !!
        GuiInventory.drawEntityOnScreen(guiLeft + 36, guiTop + 55, 30, guiLeft + 24 - mouseX, guiTop + 5 - mouseY, cat);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = this.cat.hasCustomName() ? this.cat.getCustomNameTag() : I18n.format("fatcat.gui.status.noname");
		this.fontRendererObj.drawString(name, 10, 70, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.weight")+": "+cat.getWeight()+"g", 70, 12, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.health"), 70, 34, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.hunger"), 130, 34, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.bladder"), 70, 56, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.tiredness"), 130, 56, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.friendship"), 70, 78, 0x404040);
		this.fontRendererObj.drawString(I18n.format("fatcat.gui.status.loveness"), 130, 78, 0x404040);
	}
	
	private void drawIconBar(double bar, double max, int maxNum, int x, int y, int iconX, int iconY, int emptyX, int emptyY) {
		int w = 8;
		for (int i = 0; i < maxNum; i++) {
//			System.out.println("drawIconBar(Empty): x="+(x+w*i)+",y="+y+",u="+emptyX+",v="+emptyY+",w="+w+",h="+w);
			this.drawTexturedModalRect(x+w*i, y, emptyX, emptyY, w, w);
		}
		int parX = (int)(max/maxNum);
		int limit = (int)(bar/parX);
		for (int i = 0; i < limit; i++) {
			this.drawTexturedModalRect(x+w*i, y, iconX, iconY, w, w);
		}
		int rest = ((int)((bar%parX)/parX*w));
		if (rest > 0) {
//			System.out.println("drawIconBar(last):x="+(x+w*last)+",y="+y+",u="+iconX+",v="+iconY+",w="+rest);
			this.drawTexturedModalRect(x+w*limit, y, iconX, iconY, rest, w);
		}
	}
	
}
