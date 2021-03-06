package weather2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import CoroUtil.packet.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.input.Mouse;

import weather2.client.SceneEnhancer;
import weather2.client.gui.GuiEZConfig;
import weather2.config.ConfigMisc;
import weather2.util.WeatherUtilConfig;
import weather2.weathersystem.EntityRendererProxyWeather2Mini;
import weather2.weathersystem.WeatherManagerClient;

public class ClientTickHandler
{
	
	public static World lastWorld;
	
	public static WeatherManagerClient weatherManager;
	public static SceneEnhancer sceneEnhancer;
	
	public boolean hasOpenedConfig = false;
	
	public GuiButton configButton;

	//storing old reference to help retain any modifications done by other mods (dynamic surroundings asm)
	public EntityRenderer oldRenderer;
	
	public ClientTickHandler() {
		//this constructor gets called multiple times when created from proxy, this prevents multiple inits
		if (sceneEnhancer == null) {
			sceneEnhancer = new SceneEnhancer();
			(new Thread(sceneEnhancer, "Weather2 Scene Enhancer")).start();
		}
	}

    public void onRenderScreenTick()
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	if (mc.currentScreen instanceof GuiIngameMenu) {
    		ScaledResolution scaledresolution = new ScaledResolution(mc);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
    		int k = Mouse.getX() * i / mc.displayWidth;
            int l = j - Mouse.getY() * j / mc.displayHeight - 1;
    		configButton = new GuiButton(0, (i/2)-100, 0, 200, 20, "Weather2 EZ Config");
    		configButton.drawButton(mc, k, l, 1F);
    		
    		if (k >= configButton.x && l >= configButton.y && k < configButton.x + 200 && l < configButton.y + 20) {
    			if (Mouse.isButtonDown(0)) {
    				mc.displayGuiScreen(new GuiEZConfig());
    			}
    		}
    	}
    }

    public void onTickInGUI(GuiScreen guiscreen)
    {
        //onTickInGame();
    }
    
    public void onTickInGame()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        World world = mc.world;
        
        if (ConfigMisc.Misc_proxyRenderOverrideEnabled) {
        	if (!(mc.entityRenderer instanceof EntityRendererProxyWeather2Mini)) {
				oldRenderer = mc.entityRenderer;
        		EntityRendererProxyWeather2Mini temp = new EntityRendererProxyWeather2Mini(mc, mc.getResourceManager());
		        mc.entityRenderer = temp;
        	}
    	} else {
    		if ((mc.entityRenderer instanceof EntityRendererProxyWeather2Mini)) {
    			if (oldRenderer != null) {
    				mc.entityRenderer = oldRenderer;
				} else {
					mc.entityRenderer = new EntityRenderer(mc, mc.getResourceManager());
				}

    		}
    	}

		if (world != null) {
			checkClientWeather();

			weatherManager.tick();

			if (ConfigMisc.Misc_ForceVanillaCloudsOff && world.provider.getDimension() == 0) {
				mc.gameSettings.clouds = 0;
			}

			//TODO: split logic up a bit better for this, if this is set to false mid sandstorm, fog is stuck on,
			// with sandstorms and other things it might not represent the EZ config option
			if (WeatherUtilConfig.listDimensionsWindEffects.contains(world.provider.getDimension())) {
				//weatherManager.tick();

				sceneEnhancer.tickClient();
			}

			//TODO: replace with proper client side command?
			if (mc.ingameGUI.getChatGUI().getSentMessages().size() > 0) {
				String msg = (String) mc.ingameGUI.getChatGUI().getSentMessages().get(mc.ingameGUI.getChatGUI().getSentMessages().size()-1);

				if (msg.equals("/weather2 config")) {
					mc.ingameGUI.getChatGUI().getSentMessages().remove(mc.ingameGUI.getChatGUI().getSentMessages().size()-1);
					mc.displayGuiScreen(new GuiEZConfig());
				}
			}
		} else {
			resetClientWeather();
		}

    }

    public static void resetClientWeather() {
		if (weatherManager != null) {
			Weather.dbg("Weather2: Detected old WeatherManagerClient with unloaded world, clearing its data");
			weatherManager.reset();
			weatherManager = null;
		}
	}
	
    public static void checkClientWeather() {

    	try {
			World world = FMLClientHandler.instance().getClient().world;
    		if (weatherManager == null || world != lastWorld) {
    			init(world);
        	}
    	} catch (Exception ex) {
    		Weather.dbg("Weather2: Warning, client received packet before it was ready to use, and failed to init client weather due to null world");
    	}
    }
    
    public static void init(World world) {
		//this is generally triggered when they teleport to another dimension
		if (weatherManager != null) {
			Weather.dbg("Weather2: Detected old WeatherManagerClient with active world, clearing its data");
			weatherManager.reset();
		}

		Weather.dbg("Weather2: Initializing WeatherManagerClient for client world and requesting full sync");

    	lastWorld = world;
    	weatherManager = new WeatherManagerClient(world.provider.getDimension());

		//request a full sync from server
		NBTTagCompound data = new NBTTagCompound();
		data.setString("command", "syncFull");
		data.setString("packetCommand", "WeatherData");
		Weather.eventChannel.sendToServer(PacketHelper.getNBTPacket(data, Weather.eventChannelName));
    }

    static void getField(Field field, Object newValue) throws Exception
    {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}
