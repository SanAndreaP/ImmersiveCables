/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.wire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class WireColors
{
    private static final ResourceLocation RES_LOC = new ResourceLocation(ICConstants.ID, "wire_colors.json");

    public static int ae2FluixWireColor = 0xD29BFF;
    public static int ae2DenseWireColor = 0x8C60AF;
    public static int ae2QuartzWireColor = 0xE8E2DB;

    public static int rsWireColor = 0x77C6FF;

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SideOnly(Side.CLIENT)
    public static void initialize(IResourceManager mgr) {
        try( IResource res = mgr.getResource(RES_LOC) ) {
            try( InputStream stream = res.getInputStream() ) {
                String json = IOUtils.toString(stream, Charset.forName("UTF-8"));
                WireColorsJson wcj = GSON.fromJson(json, WireColorsJson.class);

                ae2FluixWireColor = Integer.decode(wcj.ae2FluixWireColor);
                ae2DenseWireColor = Integer.decode(wcj.ae2DenseWireColor);
                ae2QuartzWireColor = Integer.decode(wcj.ae2QuartzWireColor);
                rsWireColor = Integer.decode(wcj.rsWireColor);
            }
        } catch( IOException | NumberFormatException ex ) {
            ICConstants.LOG.log(Level.WARN, "Cannot load wire colors! Reverting to defaults.", ex);

            ae2FluixWireColor = 0xD29BFF;
            ae2DenseWireColor = 0x8C60AF;
            ae2QuartzWireColor = 0xE8E2DB;
            rsWireColor = 0x77C6FF;
        }
    }

    private static class WireColorsJson {
        public String ae2FluixWireColor;
        public String ae2DenseWireColor;
        public String ae2QuartzWireColor;

        public String rsWireColor;
    }
}
