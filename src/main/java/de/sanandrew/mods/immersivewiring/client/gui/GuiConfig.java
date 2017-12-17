/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.client.gui;

import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiConfig
        extends net.minecraftforge.fml.client.config.GuiConfig
{
    public GuiConfig(GuiScreen parentScreen) {
        super(parentScreen, getCfgElements(), IWConstants.ID, "configMain", true, false, "Immersive Wiring Configuration");
    }

    private static List<IConfigElement> getCfgElements() {
        List<IConfigElement> configElements = new ArrayList<>();
        configElements.add(new ConfigElement(IWConfiguration.getCategory(IWConfiguration.CAT_AE2)));
        return configElements;
    }
}
