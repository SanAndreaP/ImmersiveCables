/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersiveintegration.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = IIConstants.ID, name = IIConstants.NAME, version = IIConstants.VERSION, dependencies = IIConstants.DEPENDENCIES)
public class ImmersiveIntegration
{
    private static final String MOD_PROXY_CLIENT = "de.sanandrew.mods.immersiveintegration.client.util.ClientProxy";
    private static final String MOD_PROXY_COMMON = "de.sanandrew.mods.immersiveintegration.util.CommonProxy";

    @Mod.Instance(IIConstants.ID)
    public static ImmersiveIntegration instance;
    @SidedProxy(modId = IIConstants.ID, clientSide = ImmersiveIntegration.MOD_PROXY_CLIENT, serverSide = ImmersiveIntegration.MOD_PROXY_COMMON)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
