/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class ModContainerRefinedFixer
        extends DummyModContainer
{
    public ModContainerRefinedFixer() {
        super(new ModMetadata());
        ModMetadata meta = super.getMetadata();
        meta.modId = ICConstants.ID + "core";
        meta.name="SanAndreasP's RS Injector for Immersive Cables";
        meta.version = "1.0";
    }
}
