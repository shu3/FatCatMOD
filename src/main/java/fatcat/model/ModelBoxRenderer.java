package fatcat.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;

class ModelBoxRenderer extends ModelRenderer
{

    float heightScale = 1.0F;
    float widthScale = 1.0F;
    float moveY = 0.0F;
    float moveX = 0.0F;
    private float boxOffsetX;
    private float boxOffsetY;
    private float boxOffsetZ;
    float rotate = 0.0F;
    boolean hide = false;
    private int displayListEx;
    private boolean compiledEx;
    private int width;
    private int height;
    private int depth;
    private String name;

    public ModelBoxRenderer(ModelBase p_i1173_1_)
    {
        super(p_i1173_1_);
    }

    public ModelBoxRenderer(ModelBase p_i1172_1_, String p_i1172_2_)
    {
        super(p_i1172_1_, p_i1172_2_);
    }

    public ModelBoxRenderer(ModelBase p_i45524_1_, int p_i45524_2_, int p_i45524_3_)
    {
        super(p_i45524_1_, p_i45524_2_, p_i45524_3_);
    }

    ModelBoxRenderer(ModelBase model, String boxName, int txOffsetX, int txOffsetY, String name, float x, float y, float z, int width, int height, int depth)
    {
        super(model, boxName);
        this.setTextureOffset(txOffsetX, txOffsetY);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.name = name;
        this.boxOffsetX = x;
        this.boxOffsetY = y;
        this.boxOffsetZ = z;
        /* don't use offset because we want to adjust other params(widthScale, rotate...etc) */
        this.addBox(this.name, 0, 0, 0, this.width, this.height, this.depth);
    }

    @Override
    public void render(float scale)
    {
        if (!this.isHidden)
        {
            if (this.showModel)
            {
                if (!compiledEx)
                {
                    this.displayListEx = GLAllocation.generateDisplayLists(1);
                    GL11.glNewList(this.displayListEx, GL11.GL_COMPILE);
                    WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();

                    for (Object aCubeList : this.cubeList)
                    {
                        ((ModelBox) aCubeList).render(worldrenderer, scale);
                    }

                    GL11.glEndList();
                    this.compiledEx = true;
                }

                GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
                GL11.glPushMatrix();
                GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                if (this.rotateAngleZ != 0.0F)
                {
                    GL11.glRotatef(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F)
                {
                    GL11.glRotatef(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F)
                {
                    GL11.glRotatef(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                }
                
                /* move offset position */
                GL11.glTranslatef(this.boxOffsetX * scale, this.boxOffsetY * scale, this.boxOffsetZ * scale);
                
                /* adjust position (consider scale) */
                if (widthScale != 1.0F || heightScale != 1.0F)
                {
                    float xDiff = (width - (width * widthScale)) / 2.0F;
                    float yDiff = (height - (height * heightScale)) / 2.0F;
                    GL11.glTranslatef(xDiff * scale, yDiff * scale, 0);
                }
                /* move */
                if (moveX != 0.0F || moveY != 0.0F)
                {
                    GL11.glTranslatef(moveX * scale, moveY * scale, 0);
                }
                if (widthScale != 1.0F || heightScale != 1.0F)
                {
                    GL11.glScalef(widthScale, heightScale, 1.0F);
                }
                /* rotate */
                if (this.rotate != 0.0F)
                {
                    GL11.glRotatef(this.rotate, 0.0F, 0.0F, 1.0F);
                }

                GL11.glCallList(this.displayListEx);
                GL11.glPopMatrix();

                GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
            }
        }
    }

}
