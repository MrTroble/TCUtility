package eu.gir.girutility.items;

import eu.gir.girutility.GirutilityMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ItemReflectiveArmor extends ItemArmor {

    public ItemReflectiveArmor(final ArmorMaterial materialIn, final int renderIndexIn,
            final EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setCreativeTab(CreativeTabs.COMBAT);
    }

    public static final ArmorMaterial reflectiveArmorMaterial = EnumHelper
            .addArmorMaterial("reflective", GirutilityMain.MODID + ":reflective", 1000, new int[] {
                    1, 1, 1, 1
            }, 30, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);

}
