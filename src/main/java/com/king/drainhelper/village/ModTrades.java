package com.king.drainhelper.village;

import com.king.drainhelper.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

/**
 * Adds the area pickaxe as a trade sold by TOOLSMITH villagers (the ones
 * that work at a smithing table), at trade level 1 (their very first tier,
 * so a fresh/unemployed villager who takes the job will offer it right away).
 *
 * NOTE FOR BEGINNERS: Fabric has changed the exact signature of
 * registerVillagerOffers a couple of times across Minecraft versions. If
 * this line shows a red squiggle, Ctrl+click (Cmd+click on Mac)
 * registerVillagerOffers to see the exact signature your version expects,
 * and adjust the lambda/arguments to match - the MerchantOffer part
 * underneath will stay the same either way.
 */
public class ModTrades {

    public static void initialize() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 1, factories -> {
            factories.add((level, entity, random) -> new MerchantOffer(
                    new ItemCost(Items.IRON_INGOT, 3),
                    new ItemStack(ModItems.AREA_PICKAXE),
                    5,      // max times this trade can be used before it locks
                    10,     // XP the villager gets per trade
                    0.05F   // price fluctuation, same as vanilla tool trades
            ));
        });
    }
}
