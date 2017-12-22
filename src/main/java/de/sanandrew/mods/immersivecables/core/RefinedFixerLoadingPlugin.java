/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.MCVersion("1.10.2")
@IFMLLoadingPlugin.DependsOn({"forge", ICConstants.ID})
@IFMLLoadingPlugin.TransformerExclusions({"de.sanandrew.mods.immersivecables"})
public class RefinedFixerLoadingPlugin
        implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                TransformNetworkNodeGraph.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return ModContainerRefinedFixer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}
