/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.client.gui;

import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ICGuiConfig
        extends GuiConfig
{
    public ICGuiConfig(GuiScreen parentScreen) {
        super(parentScreen, getCfgElements(), ICConstants.ID, "configMain", true, false, "Immersive Wiring Configuration");
    }

    private static List<IConfigElement> getCfgElements() {
        List<IConfigElement> configElements = new ArrayList<>();
        configElements.add(new ConfigElement(ICConfiguration.getCategory(ICConfiguration.CAT_AE2)));
        return configElements;
    }
}
