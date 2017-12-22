/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.client.render;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class RenderTileIWConnectable
        extends FastTESR<TileEntityImmersiveConnectable>
{
    @Override
    public void renderTileEntityFast(TileEntityImmersiveConnectable te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buffer) {
        Set<ImmersiveNetHandler.Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(te.getWorld(), Utils.toCC(te));
        if( outputs != null ) {
            Vec3d tilePosVec = new Vec3d(x, y, z);
            for( ImmersiveNetHandler.Connection con : outputs ) {
                TileEntity tileEnd = te.getWorld().getTileEntity(con.end);
                if( tileEnd instanceof IImmersiveConnectable ) {
                    if( te.getPos().toLong() > tileEnd.getPos().toLong() ) {
                        tilePosVec = tilePosVec.addVector(0.001D, 0.001D, 0.001D);
                    }
                    tessellateConnectionFast(con, te, ApiUtils.toIIC(tileEnd, te.getWorld()), con.cableType.getIcon(con), buffer, tilePosVec);
                }
            }
        }
    }

    private static void tessellateConnectionFast(ImmersiveNetHandler.Connection connection, IImmersiveConnectable start, IImmersiveConnectable end,
                                                 TextureAtlasSprite sprite, VertexBuffer buffer, Vec3d tilePos) {
        if( connection != null && start != null && end != null && connection.end != null && connection.start != null ) {
            int col = connection.cableType.getColour(connection);
            double radius = connection.cableType.getRenderDiameter() / 2.0D;
            int[] rgba = new int[] {col >> 16 & 255, col >> 8 & 255, col & 255, 255};

            Vec3d startOffset = start.getConnectionOffset(connection);
            Vec3d endOffset = end.getConnectionOffset(connection);
            if( startOffset == null ) {
                startOffset = new Vec3d(0.5D, 0.5D, 0.5D);
            }

            if( endOffset == null ) {
                endOffset = new Vec3d(0.5D, 0.5D, 0.5D);
            }

            double dx = connection.end.getX() + endOffset.x - (connection.start.getX() + startOffset.x);
            double dy = connection.end.getY() + endOffset.y - (connection.start.getY() + startOffset.y);
            double dz = connection.end.getZ() + endOffset.z - (connection.start.getZ() + startOffset.z);
            double dw = Math.sqrt(dx * dx + dz * dz);
            World world = ((TileEntity) start).getWorld();
            double rmodx = dz / dw;
            double rmodz = dx / dw;
            Vec3d[] vertex = connection.getSubVertices(world);
            Vec3d initPos = new Vec3d(startOffset.x, startOffset.y, startOffset.z);
            double uMin = sprite.getMinU();
            double uMax = sprite.getMaxU();
            double vMin = sprite.getMinV();
            double vMax = sprite.getMaxV();
            boolean vertical = connection.end.getX() == connection.start.getX() && connection.end.getZ() == connection.start.getZ();
            boolean runBackwards = dx < 0.0D && dz <= 0.0D || dz < 0.0D && dx <= 0.0D || dz < 0.0D && dx > 0.0D;
            Vec3d tilePosVec = new Vec3d(((TileEntity) start).getPos());
            if( vertical ) {
                buffer.setTranslation(tilePos.x + initPos.x, tilePos.y + initPos.y, tilePos.z + initPos.z);
                setLightmap(buffer.pos(0.0D - radius, 0.0D, 0.0D).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx - radius, dy, dz)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx + radius, dy, dz)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D + radius, 0.0D, 0.0D).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx - radius, dy, dz)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D - radius, 0.0D, 0.0D).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D + radius, 0.0D, 0.0D).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx + radius, dy, dz)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D, 0.0D, 0.0D - radius).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx, dy, dz - radius)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx, dy, dz + radius)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D, 0.0D, 0.0D + radius).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx, dy, dz - radius)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D, 0.0D, 0.0D - radius).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), tilePosVec).endVertex();
                setLightmap(buffer.pos(0.0D, 0.0D, 0.0D + radius).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), tilePosVec).endVertex();
                setLightmap(buffer.pos(dx, dy, dz + radius)      .color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), tilePosVec).endVertex();
                buffer.setTranslation(0.0D, 0.0D, 0.0D);
            } else {
                int i = runBackwards ? vertex.length - 1 : 0;

                while( true ) {
                    if( runBackwards ) {
                        if( i < 0 ) {
                            break;
                        }
                    } else if( i >= vertex.length ) {
                        break;
                    }

                    Vec3d v0 = i > 0 ? vertex[i - 1].subtract(connection.start.getX(), connection.start.getY(), connection.start.getZ()) : initPos;
                    Vec3d v1 = vertex[i].subtract(connection.start.getX(), connection.start.getY(), connection.start.getZ());

                    buffer.setTranslation(tilePos.x, tilePos.y, tilePos.z);

                    Vec3d shiftV0 = v0.add(tilePosVec);
                    Vec3d shiftV1 = v1.add(tilePosVec);
                    setLightmap(buffer.pos(v0.x, v0.y + radius, v0.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x, v1.y + radius, v1.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), shiftV1).endVertex();
                    setLightmap(buffer.pos(v1.x, v1.y - radius, v1.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), shiftV1).endVertex();
                    setLightmap(buffer.pos(v0.x, v0.y - radius, v0.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x, v1.y + radius, v1.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), shiftV1).endVertex();
                    setLightmap(buffer.pos(v0.x, v0.y + radius, v0.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), shiftV0).endVertex();
                    setLightmap(buffer.pos(v0.x, v0.y - radius, v0.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x, v1.y - radius, v1.z).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), shiftV1).endVertex();
                    setLightmap(buffer.pos(v0.x - radius * rmodx, v0.y, v0.z + radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x - radius * rmodx, v1.y, v1.z + radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), shiftV1).endVertex();
                    setLightmap(buffer.pos(v1.x + radius * rmodx, v1.y, v1.z - radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), shiftV1).endVertex();
                    setLightmap(buffer.pos(v0.x + radius * rmodx, v0.y, v0.z - radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x - radius * rmodx, v1.y, v1.z + radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMax), shiftV1).endVertex();
                    setLightmap(buffer.pos(v0.x - radius * rmodx, v0.y, v0.z + radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMax), shiftV0).endVertex();
                    setLightmap(buffer.pos(v0.x + radius * rmodx, v0.y, v0.z - radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMax, vMin), shiftV0).endVertex();
                    setLightmap(buffer.pos(v1.x + radius * rmodx, v1.y, v1.z - radius * rmodz).color(rgba[0], rgba[1], rgba[2], rgba[3]).tex(uMin, vMin), shiftV1).endVertex();
                    buffer.setTranslation(0.0D, 0.0D, 0.0D);
                    i += runBackwards ? -1 : 1;
                }
            }
        }
    }

    private static VertexBuffer setLightmap(VertexBuffer buffer, Vec3d pos) {
        BlockPos bPos = new BlockPos(pos);
        int bright = Minecraft.getMinecraft().world.getCombinedLight(bPos, 0);
        int bX = bright >> 16 & 0xFFFF;
        int bY = bright & 0xFFFF;

        buffer.lightmap(bX, bY);
        return buffer;
    }
}
