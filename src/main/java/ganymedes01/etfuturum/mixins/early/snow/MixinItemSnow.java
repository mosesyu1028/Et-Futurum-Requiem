package ganymedes01.etfuturum.mixins.early.snow;

import ganymedes01.etfuturum.blocks.SnowReplaceableFlag;
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
 *
 * @author mosesyu1028
 */
@Mixin(ItemBlock.class)
public class MixinItemSnow {

	@Inject(method = "func_150936_a", at = @At("HEAD"))
	private void onPlaceCheck(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && stack.getItem() instanceof ItemSnow && world.getBlock(x, y, z) == Blocks.snow_layer) {
			SnowReplaceableFlag.placingSnow = true;
		}
	}

	@Inject(method = "func_150936_a", at = @At("TAIL"))
	private void onPlaceCheckTail(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		SnowReplaceableFlag.placingSnow = false;
	}
}
