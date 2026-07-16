package ganymedes01.etfuturum.mixins.early.snow;

import ganymedes01.etfuturum.blocks.SnowReplaceableFlag;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sets the placingSnow flag before any placement check so BlockSnow.isReplaceable
 * allows snow-on-snow stacking regardless of layer count.
 * Also fixes placement logic for unreplaceable snow (i.e. greater than 1 layer).
 * Adjust coordinates based on side so blocks can be placed "off of" snow layers.
 *
 * @author mosesyu1028
 */
@Mixin(ItemBlock.class)
public class MixinItemSnow {

	@Inject(method = "func_150936_a", at = @At("HEAD"), cancellable = true)
	private void onPlaceCheck(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlock(x, y, z);

		if (stack != null && stack.getItem() instanceof ItemSnow && block == Blocks.snow_layer) {
			SnowReplaceableFlag.placingSnow = true;
		}

		if (block == Blocks.snow_layer && !block.isReplaceable(world, x, y, z)) {
			switch (side) {
				case 1: ++y; break;
				case 2: --z; break;
				case 3: ++z; break;
				case 4: --x; break;
				case 5: ++x; break;
			}

			cir.setReturnValue(world.canPlaceEntityOnSide(((ItemBlock) (Object) this).field_150939_a, x, y, z, false, side, (net.minecraft.entity.Entity) null, stack));
		}
	}

	@Inject(method = "func_150936_a", at = @At("TAIL"))
	private void onPlaceCheckTail(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		SnowReplaceableFlag.placingSnow = false;
	}
}
