package weather2.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import weather2.ClientProxy;
import weather2.api.WindReader;
import weather2.block.TileEntityWeatherForecast;
import weather2.weathersystem.storm.StormObject;
import CoroUtil.util.Vec3;
import extendedrenderer.ExtendedRenderer;

public class TileEntityWeatherForecastRenderer extends TileEntitySpecialRenderer
{
    public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8, int destroyStage) {
    	
    	
    	TileEntityWeatherForecast tEnt = (TileEntityWeatherForecast) var1;
    	
    	String particleCount = ExtendedRenderer.rotEffRenderer.getStatistics();
    	
    	//GL11.glColor4f(1F, 1F, 1F, 1F);
    	
    	StormObject so = tEnt.lastTickStormObject;
    	
    	Vec3 pos = new Vec3(tEnt.getPos().getX(), tEnt.getPos().getY(), tEnt.getPos().getZ());
    	
    	String descSeverity = "";
    	String descDist = "";
    	String descWindAngleCloud = "Wind Angle Clouds: " + (int)WindReader.getWindAngle(var1.getWorld(), pos, WindReader.WindType.CLOUD);
    	String descWindAngle = "Wind Angle Effect: " + (int)WindReader.getWindAngle(var1.getWorld(), pos, WindReader.WindType.DOMINANT);
    	String descWindSpeed = "Wind Speed Effect: " + (((int)(WindReader.getWindSpeed(var1.getWorld(), pos, WindReader.WindType.DOMINANT) * 100F)) / 100F);
    	
    	String progression = "";
    	
    	renderName("sdfsdfsdfsdfsdf", x, y, z);
    	
    	if (so != null) {
    		
    		progression = "Growing ";
    		if (so.hasStormPeaked) {
    			progression = "Dying ";
    		}
    		
    		if (so.levelCurIntensityStage >= StormObject.STATE_STAGE5 + 1) {
    			descSeverity = "????";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_STAGE5) {
    			descSeverity = "F5 Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Hurricane";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_STAGE4) {
    			descSeverity = "F4 Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Tropical Cyclone Stage 4";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_STAGE3) {
    			descSeverity = "F3 Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Tropical Cyclone Stage 3";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_STAGE2) {
    			descSeverity = "F2 Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Tropical Cyclone Stage 2";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_STAGE1) {
    			descSeverity = "F1 Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Tropical Cyclone Stage 1";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_FORMING) {
    			descSeverity = "Sign of Tornado";
    			if (so.stormType == StormObject.TYPE_WATER) descSeverity = "Sign of Tropical Cyclone";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_HAIL) {
    			descSeverity = "Hailstorm";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_HIGHWIND) {
    			descSeverity = "High wind";
    		} else if (so.levelCurIntensityStage >= StormObject.STATE_THUNDER) {
    			descSeverity = "Thunderstorm";
    		} else if (so.attrib_precipitation) {
    			descSeverity = "Rainstorm";
    			progression = "";
    		}
    		
    		Vec3 posXZ = new Vec3(tEnt.getPos().getX(), so.pos.yCoord, tEnt.getPos().getZ());
    		
    		descDist = "" + (int)posXZ.distanceTo(so.pos);
    	}
    	
    	int index = 1;

    	boolean oldMode = false;
    	
    	float yOffset = 2.5F;
    	
    	//if (oldMode) {
	    	renderLivingLabel("particles: " + particleCount, x, y + yOffset, z, 0, 200, 40, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	//renderLivingLabel("closest storm type: " + progression + descSeverity, x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	//renderLivingLabel("closest storm dist: " + descDist, x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	//if (so != null) renderLivingLabel("closest storm intensity: " + ((int)(so.levelCurStagesIntensity * 100F) / 100F), x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	//if (so != null) renderLivingLabel("closest storm water level: " + (so.attrib_precipitation ? so.levelWater : 0), x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	renderLivingLabel(descWindAngleCloud, x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	renderLivingLabel(descWindAngle, x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
	    	renderLivingLabel(descWindSpeed, x, y + yOffset - 0.1F * index++, z, 1, Minecraft.getMinecraft().getRenderManager().playerViewY);
    	//} else {
    		
    		float sizeSimBoxDiameter = 2048;
    		float sizeRenderBoxDiameter = 3;
    		
    		//GL11.glTranslatef((float)x + 0.5F, (float)y, (float)var6 + 0.5F);
    		
    		//renderLivingLabel("x", x, y + 1.4F, z, 1, 10, 10, Minecraft.getMinecraft().getRenderManager().playerViewY);
    		
    		GL11.glPushMatrix();
    		
    		GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var14 = Tessellator.getInstance();
            WorldRenderer worldrenderer = var14.getWorldRenderer();
            
            
    		GL11.glDisable(GL11.GL_TEXTURE_2D);
    		
    		GL11.glTranslatef((float)x + 0.5F, (float)y+1.1F, (float)z + 0.5F);
    		
            //var14.startDrawingQuads();
    		worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            //int width = var11.getStringWidth(par2Str) / 2;
        
    		worldrenderer
    		.color(0.0F, 0.0F, 0.0F, 0.65F)
    		.pos((double)-(sizeRenderBoxDiameter/2), 0, -(double)(sizeRenderBoxDiameter/2))
    		.endVertex();
    		
    		worldrenderer
    		.color(0.0F, 0.0F, 0.0F, 0.65F)
    		.pos((double)-(sizeRenderBoxDiameter/2), 0, (double)(sizeRenderBoxDiameter/2))
    		.endVertex();
    		
    		worldrenderer
    		.color(0.0F, 0.0F, 0.0F, 0.65F)
    		.pos((double)(sizeRenderBoxDiameter/2), 0, (double)(sizeRenderBoxDiameter/2))
    		.endVertex();
    		
    		worldrenderer
    		.color(0.0F, 0.0F, 0.0F, 0.65F)
    		.pos((double)(sizeRenderBoxDiameter/2), 0, -(double)(sizeRenderBoxDiameter/2))
    		.endVertex();
    		
    		var14.draw();
    		
            /*var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.65F);
            var14.addVertex((double)-(sizeRenderBoxDiameter/2), 0, -(double)(sizeRenderBoxDiameter/2));
            var14.addVertex((double)-(sizeRenderBoxDiameter/2), 0, (double)(sizeRenderBoxDiameter/2));
            var14.addVertex((double)(sizeRenderBoxDiameter/2), 0, (double)(sizeRenderBoxDiameter/2));
            var14.addVertex((double)(sizeRenderBoxDiameter/2), 0, -(double)(sizeRenderBoxDiameter/2));
            var14.draw();*/
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            
            GL11.glPopMatrix();
    		
            renderLivingLabel("\u00A7" + '6' + "|", x, y + 1.2F, z, 1, 10, 10, Minecraft.getMinecraft().getRenderManager().playerViewY);
            
    		for (int i = 0; i < tEnt.storms.size(); i++) {
    			
    			StormObject storm = tEnt.storms.get(i);
    			
    			GL11.glPushMatrix();
    			
                Vec3 posRenderOffset = new Vec3(storm.pos.xCoord - tEnt.getPos().getX(), 0, storm.pos.zCoord - tEnt.getPos().getZ());
                posRenderOffset.xCoord /= sizeSimBoxDiameter;
                posRenderOffset.zCoord /= sizeSimBoxDiameter;
                
                posRenderOffset.xCoord *= sizeRenderBoxDiameter;
                posRenderOffset.zCoord *= sizeRenderBoxDiameter;
                
                //Icon particleIcon = CommonProxy.blockWeatherDeflector.getBlockTextureFromSide(0);
                
                GL11.glTranslated(posRenderOffset.xCoord, 0, posRenderOffset.zCoord);
                
                if (storm.levelCurIntensityStage >= StormObject.STATE_FORMING) {
                	if (storm.stormType == StormObject.TYPE_WATER) {
                		renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconCyclone);
                		renderLivingLabel("C" + (int)(storm.levelCurIntensityStage - StormObject.levelStormIntensityFormingStartVal), x, y + 1.5F, z, 1, 15, 5, Minecraft.getMinecraft().getRenderManager().playerViewY);
                	} else {
                		renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconTornado);
                		renderLivingLabel("F" + (int)(storm.levelCurIntensityStage - StormObject.levelStormIntensityFormingStartVal), x, y + 1.5F, z, 1, 12, 5, Minecraft.getMinecraft().getRenderManager().playerViewY);
                	}
                } else if (storm.levelCurIntensityStage >= StormObject.STATE_HAIL) {
                	renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconHail);
                	renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconWind);
                } else if (storm.levelCurIntensityStage >= StormObject.STATE_HIGHWIND) {
                	renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconLightning);
                	renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconWind);
                } else if (storm.levelCurIntensityStage >= StormObject.STATE_THUNDER) {
                    renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconLightning);
                } else {
                	renderIcon(x, y + 1.4F, z, 16, 16, Minecraft.getMinecraft().getRenderManager().playerViewY, ClientProxy.radarIconRain);
                }
                
                if (storm.hasStormPeaked && (storm.levelCurIntensityStage > storm.STATE_NORMAL)) {
                	renderLivingLabel("\u00A7" + '4' + "|", x, y + 1.2F, z, 1, 5, 5, Minecraft.getMinecraft().getRenderManager().playerViewY);
                } else {
                	renderLivingLabel("\u00A7" + '2' + "|", x, y + 1.2F, z, 1, 5, 5, Minecraft.getMinecraft().getRenderManager().playerViewY);
                }
                
            	//renderLivingLabel("r", x, y + 1.4F, z, 1, 10, 10, Minecraft.getMinecraft().getRenderManager().playerViewY);
                
                GL11.glTranslated(-posRenderOffset.xCoord, 0, -posRenderOffset.zCoord);
                
                GL11.glPopMatrix();
    		}
    		
    	//}
    	
    	
    	
    }
    
    public void renderIcon(double par3, double par5, double par7, int width, int height, float angle, TextureAtlasSprite parIcon) {
    	
    	float var12 = 0.6F;
        float var13 = 0.016666668F * var12;
        int borderSize = 2;
    	
    	GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.5F, (float)par5, (float)par7 + 0.5F);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-var13, -var13, var13);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        Tessellator var14 = Tessellator.getInstance();
        WorldRenderer worldrenderer = var14.getWorldRenderer();
        byte var15 = 0;
        
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        //var14.startDrawingQuads();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        float f6 = parIcon.getMinU();
        float f7 = parIcon.getMaxU();
        float f9 = parIcon.getMinV();
        float f8 = parIcon.getMaxV();
        
        worldrenderer
		.color(1F, 1F, 1F, 0.6F)
		.pos((double)(-width / 2 - borderSize), (double)(-borderSize + var15), 0.0D)
		.tex(f6, f9)
		.endVertex();
        
        worldrenderer
		.color(1F, 1F, 1F, 0.6F)
		.pos((double)(-width / 2 - borderSize), (double)(height + var15), 0.0D)
		.tex(f6, f8)
		.endVertex();
        
        worldrenderer
		.color(1F, 1F, 1F, 0.6F)
		.pos((double)(width / 2 + borderSize), (double)(height + var15), 0.0D)
		.tex(f7, f8)
		.endVertex();
        
        worldrenderer
		.color(1F, 1F, 1F, 0.6F)
		.pos((double)(width / 2 + borderSize), (double)(-borderSize + var15), 0.0D)
		.tex(f7, f9)
		.endVertex();
        
        var14.draw();
        
        /*var14.setColorRGBA_F(1F, 1F, 1F, 0.6F);
        var14.addVertexWithUV((double)(-width / 2 - borderSize), (double)(-borderSize + var15), 0.0D, f6, f9);
        var14.addVertexWithUV((double)(-width / 2 - borderSize), (double)(height + var15), 0.0D, f6, f8);
        var14.addVertexWithUV((double)(width / 2 + borderSize), (double)(height + var15), 0.0D, f7, f8);
        var14.addVertexWithUV((double)(width / 2 + borderSize), (double)(-borderSize + var15), 0.0D, f7, f9);
        var14.draw();*/
        
        GL11.glDisable(GL11.GL_BLEND);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    protected void renderLivingLabel(String par2Str, double par3, double par5, double par7, int par9, float angle)
    {
    	renderLivingLabel(par2Str, par3, par5, par7, par9, 200, 80, angle);
    }
    
    protected void renderLivingLabel(String par2Str, double par3, double par5, double par7, int par9, int width, int height, float angle)
    {
        //float var10 = par1EntityLivingBase.getDistanceToEntity(this.renderManager.livingPlayer);

        int borderSize = 2;
        
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    	
        //if (var10 <= (float)par9)
        //{
            FontRenderer var11 = Minecraft.getMinecraft().getRenderManager().getFontRenderer();
            float var12 = 0.6F;
            float var13 = 0.016666668F * var12;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.5F, (float)par5, (float)par7 + 0.5F);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
            //GL11.glRotatef(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(GL11.GL_LIGHTING);
            
            if (par9 == 0) {
	            GL11.glDepthMask(false);
	            //GL11.glDisable(GL11.GL_DEPTH_TEST);
	            GL11.glEnable(GL11.GL_BLEND);
	            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	            Tessellator var14 = Tessellator.getInstance();
	            WorldRenderer worldrenderer = var14.getWorldRenderer();
	            byte var15 = 0;
	            
	            //GL11.glDisable(GL11.GL_TEXTURE_2D);
	            //var14.startDrawingQuads();
	            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	            //int width = var11.getStringWidth(par2Str) / 2;
	            
	            /*Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	            
	            Icon particleIcon = CommonProxy.blockWeatherDeflector.getBlockTextureFromSide(0);
	            
	            float f6 = particleIcon.getMinU();
	            float f7 = particleIcon.getMaxU();
	            float f8 = particleIcon.getMinV();
	            float f9 = particleIcon.getMaxV();*/
	            
	            /*var14.setColorRGBA_F(1F, 1F, 1F, 1F);
	            var14.addVertexWithUV((double)(-width / 2 - borderSize), (double)(-borderSize + var15), 0.0D, f6, f9);
	            var14.addVertexWithUV((double)(-width / 2 - borderSize), (double)(height + var15), 0.0D, f6, f8);
	            var14.addVertexWithUV((double)(width / 2 + borderSize), (double)(height + var15), 0.0D, f7, f8);
	            var14.addVertexWithUV((double)(width / 2 + borderSize), (double)(-borderSize + var15), 0.0D, f7, f9);*/
            
	            
	            
	            worldrenderer
	    		.color(0.0F, 0.0F, 0.0F, 0.25F)
	    		.pos((double)(-width / 2 - borderSize), (double)(-borderSize + var15), 0.0D)
	    		
	    		.endVertex();
	            
	            worldrenderer
	    		.color(0.0F, 0.0F, 0.0F, 0.25F)
	    		.pos((double)(-width / 2 - borderSize), (double)(height + var15), 0.0D)
	    		
	    		.endVertex();
	            
	            worldrenderer
	    		.color(0.0F, 0.0F, 0.0F, 0.25F)
	    		.pos((double)(width / 2 + borderSize), (double)(height + var15), 0.0D)
	    		
	    		.endVertex();
	            
	            worldrenderer
	    		.color(0.0F, 0.0F, 0.0F, 0.25F)
	    		.pos((double)(width / 2 + borderSize), (double)(-borderSize + var15), 0.0D)
	    		
	    		.endVertex();
	            
	            /*var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
	            var14.addVertex((double)(-width / 2 - borderSize), (double)(-borderSize + var15), 0.0D);
	            var14.addVertex((double)(-width / 2 - borderSize), (double)(height + var15), 0.0D);
	            var14.addVertex((double)(width / 2 + borderSize), (double)(height + var15), 0.0D);
	            var14.addVertex((double)(width / 2 + borderSize), (double)(-borderSize + var15), 0.0D);*/
	            
	            var14.draw();
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            //var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var11.drawString(par2Str, -width/2+borderSize/*-var11.getStringWidth(par2Str) / 2*/, 0, 0xFFFFFF);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        //}
            
            GL11.glEnable(GL11.GL_CULL_FACE);
    }
    
    public void renderName(String test, double x, double y, double z)
    {
    	
    	TextureAtlasSprite parIcon = ClientProxy.radarIconCyclone;
        
        float f6 = parIcon.getMinU();
        float f7 = parIcon.getMaxU();
        float f9 = parIcon.getMinV();
        float f8 = parIcon.getMaxV();
    	
        float f = 64F;
        
        String s = test;
        float f1 = 0.02666667F;
        GlStateManager.alphaFunc(516, 0.1F);

        FontRenderer fontrenderer = Minecraft.getMinecraft().getRenderManager().getFontRenderer();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 2 + 0.5F - (/*entity.isChild() ? entity.height / 2.0F : */0.0F), (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
        GlStateManager.translate(0.0F, 9.374999F, 0.0F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = fontrenderer.getStringWidth(s) / 2;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        
        //Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        //worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        worldrenderer.pos((double)(-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.75F)/*.tex(f6, f9)*/.endVertex();
        worldrenderer.pos((double)(-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.75F)/*.tex(f6, f8)*/.endVertex();
        worldrenderer.pos((double)(i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.75F)/*.tex(f7, f8)*/.endVertex();
        worldrenderer.pos((double)(i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.75F)/*.tex(f7, f9)*/.endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
