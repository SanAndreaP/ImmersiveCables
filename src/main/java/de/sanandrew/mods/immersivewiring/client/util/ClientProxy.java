/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.client.util;

import de.sanandrew.mods.immersivewiring.util.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy
        extends CommonProxy
{
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModelRegistry.registerModelPre112();
    }
}
