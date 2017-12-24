/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.client.util;

import de.sanandrew.mods.immersivecables.util.CommonProxy;
import de.sanandrew.mods.immersivecables.wire.WireColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy
        extends CommonProxy
        implements IResourceManagerReloadListener
{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        WireColors.initialize(Minecraft.getMinecraft().getResourceManager());
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        WireColors.initialize(resourceManager);
    }
}
