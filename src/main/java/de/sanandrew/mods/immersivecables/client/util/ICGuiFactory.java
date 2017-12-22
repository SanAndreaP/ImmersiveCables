/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.client.util;

import de.sanandrew.mods.immersivecables.client.gui.ICGuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ICGuiFactory
        implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraftInstance) { }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ICGuiConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    @Deprecated
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
