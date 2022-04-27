package eu.gir.girutility.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemReflectiveArmor extends ItemArmor {

    public ItemReflectiveArmor(ArmorMaterial materialIn, int renderIndexIn,
            EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setCreativeTab(CreativeTabs.COMBAT);
    }

}
