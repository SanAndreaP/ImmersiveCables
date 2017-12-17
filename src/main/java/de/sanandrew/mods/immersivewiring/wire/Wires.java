/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.WireType;

public enum Wires
{
    FLUIX(new FluixWire()),
    FLUIX_DENSE(new FluixDenseWire()),
    QUARTZ(new QuartzWire());

    public static final Wires[] VALUES = values();

    public final String key = this.name().toLowerCase();
    public final WireType type;

    Wires(WireType type) {
        this.type = type;
    }

    public WireType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
