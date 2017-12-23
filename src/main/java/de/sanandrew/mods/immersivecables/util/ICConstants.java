/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ICConstants
{
    static final String COMPAT_APPLIEDENERGISTICS = "appliedenergistics2";
    static final String COMPAT_REFINEDSTORAGE = "refinedstorage";

    public static final String ID = "immersivecables";
    public static final Logger LOG = LogManager.getLogger(ID);
    public static final String VERSION = "1.0.1";
    public static final String NAME = "Immersive Wiring";
    public static final String DEPENDENCIES = "required-after:Forge@[12.18.3.2511,];required-after:immersiveengineering@[0.10-61,];" +
                                                      "after:" + COMPAT_APPLIEDENERGISTICS + ';' +
                                                      "after:" + COMPAT_REFINEDSTORAGE;
}
