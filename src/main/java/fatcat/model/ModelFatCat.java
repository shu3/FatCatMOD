package fatcat.model;

import java.math.BigDecimal;

import org.lwjgl.opengl.GL11;

import fatcat.EntityFatCat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelFatCat extends ModelBase {
    /** The back left leg model for the FatCat. */
    ModelRenderer backLeftLeg;
    /** The back right leg model for the FatCat. */
    ModelRenderer backRightLeg;
    /** The front left leg model for the FatCat. */
    ModelRenderer frontLeftLeg;
    /** The front right leg model for the FatCat. */
    ModelRenderer frontRightLeg;
    /** The tail model for the FatCat. */
    ModelRenderer tail;
    /** The head model for the FatCat. */
    ModelRenderer head;
    /** The body model for the FatCat. */
    ModelRenderer body;
    /** facial expression **/
    ModelBoxRenderer eye1;
    ModelBoxRenderer eye2;
    ModelBoxRenderer mouth;
    ModelBoxRenderer ripLeft;
    ModelBoxRenderer ripRight; 
    public boolean hide = false;
    int pose = 1;
    
    static private float BODY_POS_Y = 10.0F;
    static private float HEAD_POS_Y = 12.0F;
    static private float TAIL_POS_Y = 14F;
    static public float HEIGHT = 24.0F;
    static public float WIDTH = 14.0F;

    public ModelFatCat()
    {
    	textureWidth = 64;
    	textureHeight = 32;
    	setTextureOffset("head.main", 0, 0);
    	setTextureOffset("head.ear1", 58, 0);
    	setTextureOffset("head.ear2", 58, 2);
        setTextureOffset("head.eye1", 0, 30);
        setTextureOffset("head.eye2", 0, 30);
        setTextureOffset("head.ripLeft", 0, 30);
        setTextureOffset("head.ripRight", 0, 30);
        setTextureOffset("head.mouth", 0, 30);

    	body = new ModelRenderer(this, 8, 7);
    	body.addBox(0F, 0F, 0F, 14, 11, 14);
    	body.setRotationPoint(-8F, BODY_POS_Y, -6F);
    	body.setTextureSize(64, 32);
    	body.mirror = true;
    	setRotation(body, 0F, 0F, 0F);
    	tail = new ModelRenderer(this, 0, 13);
    	tail.addBox(-2F, 0F, -2F, 4, 3, 4);
    	tail.setRotationPoint(-1F, TAIL_POS_Y, 7F);
    	tail.setTextureSize(64, 32);
    	tail.mirror = true;
    	setRotation(tail, 1.660128F, 0F, 0F);
    	frontLeftLeg = new ModelRenderer(this, 50, 0);
    	frontLeftLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
    	frontLeftLeg.setRotationPoint(3F, 20F, -4F);
    	frontLeftLeg.setTextureSize(64, 32);
    	frontLeftLeg.mirror = true;
    	setRotation(frontLeftLeg, 0F, 0F, 0F);
    	frontRightLeg = new ModelRenderer(this, 42, 0);
    	frontRightLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
    	frontRightLeg.setRotationPoint(-5F, 20F, -4F);
    	frontRightLeg.setTextureSize(64, 32);
    	frontRightLeg.mirror = true;
    	setRotation(frontRightLeg, 0F, 0F, 0F);
    	backLeftLeg = new ModelRenderer(this, 34, 0);
    	backLeftLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
    	backLeftLeg.setRotationPoint(3F, 20F, 6F);
    	backLeftLeg.setTextureSize(64, 32);
    	backLeftLeg.mirror = true;
    	setRotation(backLeftLeg, 0F, 0F, 0F);
    	backRightLeg = new ModelRenderer(this, 0, 21);
    	backRightLeg.addBox(-1F, 0F, -1F, 2, 4, 2);
    	backRightLeg.setRotationPoint(-5F, 20F, 6F);
    	backRightLeg.setTextureSize(64, 32);
    	backRightLeg.mirror = true;
    	setRotation(backRightLeg, 0F, 0F, 0F);
    	head = new ModelRenderer(this, "head");
    	head.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(head, 0F, 0F, 0F);
    	head.mirror = false;
    	head.addBox("main", -5F, -3F, -4F, 10, 6, 5);
    	head.addBox("ear1", -4F, -4F, -3F, 2, 1, 1);
    	head.addBox("ear2", 2F, -4F, -3F, 2, 1, 1);
    	eye1 = new ModelBoxRenderer(this, "head", 0, 30, "eye1", -3F, -1F, -4.01F, 1, 1, 1);
    	eye1.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(eye1, 0F, 0F, 0F);
    	eye2 = new ModelBoxRenderer(this, "head", 0, 30, "eye2", 2F, -1F, -4.01F, 1, 1, 1);
    	eye2.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(eye2, 0F, 0F, 0F);
    	ripLeft = new ModelBoxRenderer(this, "head", 0, 30, "ripLeft", -0.20F, 1.00F, -4.01F, 2, 1, 1);
    	ripLeft.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(ripLeft, 0F, 0F, 0F);
    	ripLeft.heightScale = ripLeft.widthScale = 0.5F;
    	ripLeft.rotate = 135;
    	ripRight = new ModelBoxRenderer(this, "head", 0, 30, "ripRight", -0.45F, 0.60F, -4.01F, 2, 1, 1);
    	ripRight.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(ripRight, 0F, 0F, 0F);
    	ripRight.heightScale = ripRight.widthScale = 0.5F;
    	ripRight.rotate = 45F;
    	mouth = new ModelBoxRenderer(this, "head", 0, 30, "mouth", -0.5F, 1F, -4.01F, 1, 1, 1);
    	mouth.setRotationPoint(-1F, HEAD_POS_Y, -5F);
    	setRotation(mouth, 0F, 0F, 0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity cat, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, cat);
        float scale = ((EntityFatCat)cat).getCatScale();
        float base = 0.0F;
        base = (HEIGHT-(HEIGHT*scale));
//         System.out.println("ModelFatCat(render): scale="+scale+",weight="+((EntityFatCat)cat).getWeight()+",height="+((EntityFatCat)cat).height);
        GL11.glPushMatrix();
        // !! must put translate line before scalef !!
        GL11.glTranslatef(0.0F, base*p_78088_7_, 0.0F);
        GL11.glScalef(scale, scale, scale);
        this.head.render(p_78088_7_);
        this.body.render(p_78088_7_);
        this.tail.render(p_78088_7_);
        this.backLeftLeg.render(p_78088_7_);
        this.backRightLeg.render(p_78088_7_);
        this.frontLeftLeg.render(p_78088_7_);
        this.frontRightLeg.render(p_78088_7_);
        this.eye1.render(p_78088_7_);
        this.eye2.render(p_78088_7_);
        if (!this.ripLeft.hide) {
        	this.ripLeft.render(p_78088_7_);
        }
        if (!this.ripRight.hide) {
        	this.ripRight.render(p_78088_7_);
        }
        this.mouth.render(p_78088_7_);
        GL11.glPopMatrix();
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float time, float walkSpeed, float p_78087_3_, float rotationYaw, float rotationPitch, float p_78087_6_, Entity p_78087_7_)
    {
        EntityFatCat entityFatCat = (EntityFatCat)p_78087_7_;
        if (entityFatCat.getFace() != EntityFatCat.Face.Sleep) {
        	this.head.rotateAngleX = (rotationPitch / (180F / (float)Math.PI));
        	this.head.rotateAngleY = (rotationYaw / (180F / (float)Math.PI));
        	// limit horizontal neck swing
        	this.head.rotateAngleY = Math.max(Math.min(this.head.rotateAngleY, 0.5F), -0.5F);
        }
        else {
            this.head.rotateAngleX = 0;
            this.head.rotateAngleY = 0;
        	
        }

        if (this.pose != 3)
        {

            if (this.pose == 2)
            {
                this.backLeftLeg.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.0F * walkSpeed;
                this.backRightLeg.rotateAngleX = MathHelper.cos(time * 0.6662F + 0.3F) * 1.0F * walkSpeed;
                this.frontLeftLeg.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI + 0.3F) * 1.0F * walkSpeed;
                this.frontRightLeg.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.0F * walkSpeed;
            }
            else
            {
                this.backLeftLeg.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.0F * walkSpeed;
                this.backRightLeg.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.0F * walkSpeed;
                this.frontLeftLeg.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.0F * walkSpeed;
                this.frontRightLeg.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.0F * walkSpeed;

                if (this.pose == 1)
                {
                    this.tail.rotateAngleX = 1.7278761F + ((float)Math.PI / 4F) * MathHelper.cos(time) * walkSpeed;
                }
                else
                {
                    this.tail.rotateAngleX = 1.7278761F + 0.47123894F * MathHelper.cos(time) * walkSpeed;
                }
            }
        }
        if (entityFatCat.getPose() == EntityFatCat.Pose.Shit) {
        	this.tail.rotateAngleX = 3.2F;
        }
        syncFaceOnHead();
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entity, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        EntityFatCat entityFatCat = (EntityFatCat)entity;
        this.head.rotationPointY = HEAD_POS_Y;
        this.body.rotationPointY = BODY_POS_Y;
        this.tail.rotationPointY = TAIL_POS_Y;
        this.head.rotateAngleZ = entityFatCat.getInterestedAngle(p_78086_4_);

        if (entityFatCat.isSneaking())
        {
            ++this.body.rotationPointY;
            this.head.rotationPointY += 2.0F;
            this.pose = 0;
        }
        else if (entityFatCat.isSprinting())
        {
        	// TODO: set sprinting animation
            this.pose = 2;
        }
        else if (entityFatCat.isSitting())
        {
        	this.tail.rotationPointY += 2.0F;
            this.body.rotationPointY += 2.0F;
            this.head.rotationPointY += 5.0F;
            this.frontLeftLeg.rotateAngleX = 0;
            this.frontRightLeg.rotateAngleX = 0;
            this.backLeftLeg.rotateAngleX = 0;
            this.backRightLeg.rotateAngleX = 0;
            this.pose = 3;
        }
        else
        {
            this.pose = 1;
        }
        syncFaceOnHead();

        
        if (entityFatCat.getFace() == EntityFatCat.Face.Blink) {
        	eye1.heightScale = eye2.heightScale = 0.2F;
        }
        else if (entityFatCat.getFace() == EntityFatCat.Face.Sleep) {
        	eye1.heightScale = 0.5F;
        	eye1.widthScale = 1.5F;
        	eye2.heightScale = 0.5F;
        	eye2.widthScale = 1.5F;
        	eye2.moveY = 0.5F;
        	eye1.moveY = 0.5F;
        }
        else if (entityFatCat.getFace() == EntityFatCat.Face.Shit) {
        	eye1.moveY = (entityFatCat.getRNG().nextFloat()/2 - 0.5F);
        	eye2.moveX = (entityFatCat.getRNG().nextFloat()/2 - 0.5F);
        	eye2.moveY = (entityFatCat.getRNG().nextFloat()/2 - 0.5F);
        	eye1.moveY = (entityFatCat.getRNG().nextFloat()/2 - 0.5F);
        	eye2.moveY += 0.5F;
        	eye1.moveY += 0.5F;
        	mouth.moveY = ripLeft.moveY = ripRight.moveY = 0.5F;
        }
        else if (entityFatCat.getFace() == EntityFatCat.Face.Myau) {
        	mouth.moveY = -0.2F;
        	mouth.heightScale = 1.5F;
        	mouth.widthScale = 1.5F;
        	ripRight.hide = ripLeft.hide = true;
        }
        else if (entityFatCat.getFace() == EntityFatCat.Face.Baymax) {
        	eye1.moveY = eye2.moveY = 0.5F;
        	mouth.moveY = -1.5F;
        	eye1.heightScale = eye2.heightScale = eye1.widthScale = eye2.widthScale = 2.0F;
        	mouth.heightScale = 0.5F;
        	mouth.widthScale = 2.0F;
        }
        else {
        	eye1.moveX = eye1.moveY = eye2.moveX = eye2.moveY = mouth.moveX = mouth.moveY = ripLeft.moveX = ripLeft.moveY = ripRight.moveX = ripRight.moveY = 0.0F;
        	ripLeft.heightScale = ripRight.heightScale = 0.5F;
        	ripRight.hide = ripLeft.hide = false;
        	mouth.heightScale = mouth.widthScale = 0.0F;
        	eye1.heightScale = eye1.widthScale = eye2.heightScale = eye2.widthScale = 1.0F;
        }
//        System.out.println("ModelFatCat: interestingAngle = " + entityFatCat.getInterestedAngle(p_78086_4_) + ", tryBeg=" + entityFatCat.tryBeg);
    }

    
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
    }
    
    private void syncFaceOnHead() {
        this.eye1.rotationPointY = this.eye2.rotationPointY = this.mouth.rotationPointY = this.ripRight.rotationPointY  = this.ripLeft.rotationPointY = this.head.rotationPointY;
        this.eye1.rotateAngleX = this.eye2.rotateAngleX = this.mouth.rotateAngleX = this.ripRight.rotateAngleX = this.ripLeft.rotateAngleX = this.head.rotateAngleX;
        this.eye1.rotateAngleY = this.eye2.rotateAngleY = this.mouth.rotateAngleY = this.ripRight.rotateAngleY = this.ripLeft.rotateAngleY = this.head.rotateAngleY;
        this.eye1.rotateAngleZ = this.eye2.rotateAngleZ = this.mouth.rotateAngleZ = this.ripRight.rotateAngleZ = this.ripLeft.rotateAngleZ = this.head.rotateAngleZ;
    }
    
    private float to_angle(float f) {
    	return f * (180F / (float)Math.PI);
    }
}
