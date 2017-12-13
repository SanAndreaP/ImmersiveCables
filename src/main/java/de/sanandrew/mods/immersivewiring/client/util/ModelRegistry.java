/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivewiring.client.util;

import de.sanandrew.mods.immersivewiring.block.BlockAeFluixCoil;
import de.sanandrew.mods.immersivewiring.block.BlockRegistry;
import de.sanandrew.mods.immersivewiring.block.BlockRelayFluix;
import de.sanandrew.mods.immersivewiring.client.render.RenderTileIWConnectable;
import de.sanandrew.mods.immersivewiring.item.ItemRegistry;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityMETransformer;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public final class ModelRegistry
{
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) throws Exception {
//        setStandardModel(BlockRegistry.FLUX_COIL);

        Item blockItem = Item.getItemFromBlock(BlockRegistry.FLUIX_COIL);
        if( blockItem != null ) {
            ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(IWConstants.ID + ':' + BlockAeFluixCoil.Coil.FLUIX_COIL, "inventory"));
            ModelLoader.setCustomModelResourceLocation(blockItem, 1, new ModelResourceLocation(IWConstants.ID + ':' + BlockAeFluixCoil.Coil.FLUIX_COIL_DENSE, "inventory"));
        }
        blockItem = Item.getItemFromBlock(BlockRegistry.RELAY_FLUIX);
        if( blockItem != null ) {
            ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(IWConstants.ID + ":relay_" + BlockRelayFluix.Type.FLUIX, "inventory"));
            ModelLoader.setCustomModelResourceLocation(blockItem, 1, new ModelResourceLocation(IWConstants.ID + ":relay_" + BlockRelayFluix.Type.FLUIX_DENSE, "inventory"));
        }

        ModelLoader.setCustomModelResourceLocation(ItemRegistry.WIRE_COIL, 0, new ModelResourceLocation(IWConstants.ID + ":wire_" + WireRegistry.Wire.FLUIX, "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistry.WIRE_COIL, 1, new ModelResourceLocation(IWConstants.ID + ":wire_" + WireRegistry.Wire.FLUIX_DENSE, "inventory"));
//        setCustomMeshModel(ItemRegistry.TURRET_PLACER, new MeshDefUUID.Turret());
//        setCustomMeshModel(ItemRegistry.TURRET_AMMO, new MeshDefUUID.Ammo());
//        setCustomMeshModel(ItemRegistry.TURRET_UPGRADE, new MeshDefUUID.Upgrade());
//        setCustomMeshModel(ItemRegistry.REPAIR_KIT, new MeshDefUUID.Repkit());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMETransformer.class, new RenderTileIWConnectable());
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectrolyteGenerator.class, new RenderElectrolyteGenerator());
    }

    public static void registerModelPre112() {
        Item blockItem = Item.getItemFromBlock(BlockRegistry.FLUIX_COIL);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem,
                                             new ResourceLocation(IWConstants.ID, BlockAeFluixCoil.Coil.FLUIX_COIL.getName()),
                                             new ResourceLocation(IWConstants.ID, BlockAeFluixCoil.Coil.FLUIX_COIL_DENSE.getName()));
        }
        blockItem = Item.getItemFromBlock(BlockRegistry.RELAY_FLUIX);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem,
                                             new ResourceLocation(IWConstants.ID, "relay_" + BlockRelayFluix.Type.FLUIX.getName()),
                                             new ResourceLocation(IWConstants.ID, "relay_" + BlockRelayFluix.Type.FLUIX_DENSE.getName()));
        }

        ModelBakery.registerItemVariants(ItemRegistry.WIRE_COIL,
                                         new ResourceLocation(IWConstants.ID, "wire_" + WireRegistry.Wire.FLUIX),
                                         new ResourceLocation(IWConstants.ID, "wire_" + WireRegistry.Wire.FLUIX_DENSE));
//        setCustomMeshModel(ItemRegistry.TURRET_PLACER, new MeshDefUUID.Turret());
//        setCustomMeshModel(ItemRegistry.TURRET_AMMO, new MeshDefUUID.Ammo());
//        setCustomMeshModel(ItemRegistry.TURRET_UPGRADE, new MeshDefUUID.Upgrade());
//        setCustomMeshModel(ItemRegistry.REPAIR_KIT, new MeshDefUUID.Repkit());
    }

    private static void setStandardModel(Item item) {
        ResourceLocation regName = item.getRegistryName();
        if( regName != null ) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(regName, "inventory"));
        }
    }

    private static void setStandardModel(Block item) {
        if( item != Blocks.AIR ) {
            setStandardModel(Item.getItemFromBlock(item));
        }
    }

//    private static void setCustomMeshModel(Item item, MeshDefUUID<?> mesher) {
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, mesher);
//        ModelLoader.setCustomMeshDefinition(item, mesher);
//        ModelBakery.registerItemVariants(item, mesher.getResLocations());
//    }

//    private static abstract class MeshDefUUID<T>
//            implements ItemMeshDefinition
//    {
//        public final Map<UUID, ModelResourceLocation> modelRes = new HashMap<>();
//
//        @Override
//        public ModelResourceLocation getModelLocation(ItemStack stack) {
//            T type = getType(stack);
//            ResourceLocation regName = stack.getItem().getRegistryName();
//            if( regName != null ) {
//                return type != null ? this.modelRes.get(getId(type)) : new ModelResourceLocation(regName, "inventory");
//            } else {
//                return null;
//            }
//        }
//
//        public abstract T getType(ItemStack stack);
//        public abstract UUID getId(T type);
//
//        public ResourceLocation[] getResLocations() {
//            return this.modelRes.values().toArray(new ModelResourceLocation[this.modelRes.size()]);
//        }
//
//        static final class Turret
//                extends MeshDefUUID<ITurret>
//        {
//            Turret() {
//                for( ITurret info : TurretRegistry.INSTANCE.getTurrets() ) {
//                    ModelResourceLocation modelRes = new ModelResourceLocation(info.getItemModel(), "inventory");
//                    this.modelRes.put(info.getId(), modelRes);
//                }
//            }
//
//            @Override
//            public ITurret getType(ItemStack stack) { return TurretRegistry.INSTANCE.getTurret(stack); }
//
//            @Override
//            public UUID getId(ITurret type) { return type.getId(); }
//        }
//
//        static final class Ammo
//                extends MeshDefUUID<IAmmunition>
//        {
//            public Ammo() {
//                for( IAmmunition ammo : AmmunitionRegistry.INSTANCE.getRegisteredTypes() ) {
//                    ModelResourceLocation modelRes = new ModelResourceLocation(ammo.getModel(), "inventory");
//                    this.modelRes.put(ammo.getId(), modelRes);
//                }
//            }
//
//            @Override
//            public IAmmunition getType(ItemStack stack) { return AmmunitionRegistry.INSTANCE.getType(stack); }
//
//            @Override
//            public UUID getId(IAmmunition type) { return type.getId(); }
//        }
//
//        static final class Upgrade
//                extends MeshDefUUID<ITurretUpgrade>
//        {
//            public Upgrade() {
//                for( ITurretUpgrade upg : UpgradeRegistry.INSTANCE.getUpgrades() ) {
//                    ModelResourceLocation modelRes = new ModelResourceLocation(upg.getModel(), "inventory");
//                    this.modelRes.put(UpgradeRegistry.INSTANCE.getUpgradeId(upg), modelRes);
//                }
//            }
//
//            @Override
//            public ITurretUpgrade getType(ItemStack stack) { return UpgradeRegistry.INSTANCE.getUpgrade(stack); }
//
//            @Override
//            public UUID getId(ITurretUpgrade type) { return UpgradeRegistry.INSTANCE.getUpgradeId(type); }
//        }
//
//        static final class Repkit
//                extends MeshDefUUID<TurretRepairKit>
//        {
//            public Repkit() {
//                for( TurretRepairKit kit : RepairKitRegistry.INSTANCE.getRegisteredTypes() ) {
//                    ModelResourceLocation modelRes = new ModelResourceLocation(kit.getModel(), "inventory");
//                    this.modelRes.put(RepairKitRegistry.INSTANCE.getTypeId(kit), modelRes);
//                }
//            }
//
//            @Override
//            public TurretRepairKit getType(ItemStack stack) { return RepairKitRegistry.INSTANCE.getType(stack); }
//
//            @Override
//            public UUID getId(TurretRepairKit type) { return RepairKitRegistry.INSTANCE.getTypeId(type); }
//        }
//    }
}
