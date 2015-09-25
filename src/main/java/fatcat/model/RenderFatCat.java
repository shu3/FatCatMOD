package fatcat.model;

import java.math.BigDecimal;

import org.lwjgl.opengl.GL11;

import scala.reflect.internal.Trees.CaseDef;
import fatcat.EntityFatCat;
import fatcat.FatCatMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderFatCat extends RendererLivingEntity {
	public static int SKIN_COUNT = 3;

	public RenderFatCat(RenderManager manager) {
		super(manager, new ModelFatCat(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity cat) {
		int type = ((EntityFatCat)cat).getSkinType();
		String location = FatCatMod.instance.skinMap.get(type);
		if (null != location) {
			return new ResourceLocation(location);
		} else {
			return new ResourceLocation(FatCatMod.instance.skinMap.get(0));
		}
	}

    protected void renderLeash(EntityLiving cat, double x, double y, double z, float p_110827_8_, float p_110827_9_)
    {
        Entity entity = cat.getLeashedToEntity();

        if (entity != null)
        {
            y -= (1.6D - (double)cat.height) * 0.5D;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer renderer = tessellator.getWorldRenderer();
            double d3 = this.func_110828_a((double)entity.prevRotationYaw, (double)entity.rotationYaw, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double d4 = this.func_110828_a((double)entity.prevRotationPitch, (double)entity.rotationPitch, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double d5 = Math.cos(d3);
            double d6 = Math.sin(d3);
            double d7 = Math.sin(d4);

            if (entity instanceof EntityHanging)
            {
                d5 = 0.0D;
                d6 = 0.0D;
                d7 = -1.0D;
            }

            double d8 = Math.cos(d4);
            double d9 = this.func_110828_a(entity.prevPosX, entity.posX, (double)p_110827_9_) - d5 * 0.7D - d6 * 0.5D * d8;
            double d10 = this.func_110828_a(entity.prevPosY + (double)entity.getEyeHeight() * 0.7D, entity.posY + (double)entity.getEyeHeight() * 0.7D, (double)p_110827_9_) - d7 * 0.5D - 0.25D;
            double d11 = this.func_110828_a(entity.prevPosZ, entity.posZ, (double)p_110827_9_) - d6 * 0.7D + d5 * 0.5D * d8;
            double d12 = this.func_110828_a((double)cat.prevRenderYawOffset, (double)cat.renderYawOffset, (double)p_110827_9_) * 0.01745329238474369D + (Math.PI / 2D);
            d5 = Math.cos(d12) * (double)cat.width * 0.4D;
            d6 = Math.sin(d12) * (double)cat.width * 0.4D;
            double d13 = this.func_110828_a(cat.prevPosX, cat.posX, (double)p_110827_9_) + d5;
            double d14 = this.func_110828_a(cat.prevPosY, cat.posY, (double)p_110827_9_);
            double d15 = this.func_110828_a(cat.prevPosZ, cat.posZ, (double)p_110827_9_) + d6;
            x += d5;
            z += d6;
            double d16 = (double)((float)(d9 - d13));
            double d17 = (double)((float)(d10 - d14));
            double d18 = (double)((float)(d11 - d15));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            boolean flag = true;
            double d19 = 0.025D;
            renderer.startDrawing(5);
            int i;
            float f2;

            for (i = 0; i <= 24; ++i)
            {
                if (i % 2 == 0)
                {
                	// setColorRGBA_F
                    renderer.func_178960_a(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                	// setColorRGBA_F
                    renderer.func_178960_a(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                f2 = (float)i / 24.0F;
                renderer.addVertex(x + d16 * (double)f2 + 0.0D, y + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), z + d18 * (double)f2);
                renderer.addVertex(x + d16 * (double)f2 + 0.025D, y + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, z + d18 * (double)f2);
            }

            tessellator.draw();
            renderer.startDrawing(5);

            for (i = 0; i <= 24; ++i)
            {
                if (i % 2 == 0)
                {
                	// setColorRGBA_F
                    renderer.func_178960_a(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                	// setColorRGBA_F
                    renderer.func_178960_a(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                f2 = (float)i / 24.0F;
                renderer.addVertex(x + d16 * (double)f2 + 0.0D, y + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, z + d18 * (double)f2);
                renderer.addVertex(x + d16 * (double)f2 + 0.025D, y + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), z + d18 * (double)f2 + 0.025D);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }


    private double func_110828_a(double p_110828_1_, double p_110828_3_, double p_110828_5_)
    {
        return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
    }

	@Override
	public void doRender(Entity cat, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		// adjust shadow size
		this.shadowSize = (new BigDecimal(((EntityFatCat)cat).getWeight() / 9000.0F)).setScale(1, BigDecimal.ROUND_DOWN).floatValue();
		this.shadowSize = Math.max(0.1F, shadowSize);
		super.doRender(cat, x, y, z, p_76986_8_, p_76986_9_);
		// down leash
		renderLeash((EntityFatCat)cat, x, y-0.4D, z, p_76986_8_, p_76986_9_);
	}

	@Override
	/*
	 * Sent rotation of body
	 * @see net.minecraft.client.renderer.entity.RendererLivingEntity#rotateCorpse(net.minecraft.entity.EntityLivingBase, float, float, float)
	 */
    protected void rotateCorpse(EntityLivingBase entity, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
		EntityFatCat cat = (EntityFatCat)entity;

        if (cat.isEntityAlive() && cat.getPose() == EntityFatCat.Pose.Brushing)
        {
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
        else
        {
            super.rotateCorpse(cat, p_77043_2_, p_77043_3_, p_77043_4_);
        }
    }

	@Override
    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase entity, double p_77039_2_, double p_77039_4_, double p_77039_6_)
    {
		EntityFatCat cat = (EntityFatCat)entity;
		
        if (cat.isEntityAlive() && cat.getPose() == EntityFatCat.Pose.Brushing)
        {
            super.renderLivingAt(cat, p_77039_2_, p_77039_4_ + 0.25F, p_77039_6_);
        }
        else
        {
            super.renderLivingAt(cat, p_77039_2_, p_77039_4_, p_77039_6_);
        }
    }
	
	@Override
	protected boolean canRenderName(EntityLivingBase targetEntity) {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.field_147941_i);
	}

	 /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLiving targetEntity)
    {
    	return canRenderName((EntityLivingBase)targetEntity);
    }

}
