/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.wire;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Wires
        implements IStringSerializable
{
    FLUIX(new FluixWire()),
    FLUIX_DENSE(new FluixDenseWire()),
    QUARTZ(new QuartzWire()),
    REFINED(new RefinedWire());

    public static final Wires[] VALUES = values();

    public final String key = this.name().toLowerCase(Locale.ROOT);
    public final WireType type;

    Wires(WireType type) {
        this.type = type;
    }

    public WireType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
