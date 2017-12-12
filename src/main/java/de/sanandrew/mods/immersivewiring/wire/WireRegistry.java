/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.WireType;

public final class WireRegistry
{
    public static void initialize() {
        Wire.FLUIX.type = new FluixWire();
        Wire.FLUIX_DENSE.type = new FluixDenseWire();
    }

    public enum Wire
    {
        FLUIX,
        FLUIX_DENSE;

        public static final Wire[] VALUES = values();

        public final String key = this.name().toLowerCase();
        private WireType type;

        public WireType getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
