package ganymedes01.etfuturum.mixins.early.snow;

import ganymedes01.etfuturum.blocks.SnowReplaceableFlag;
import net.minecraft.block.BlockSnow;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Only the thinnest snow layer (meta 0) is replaceable by other blocks.
 * Snow-on-snow stacking sets placingSnow=true to bypass this restriction.
 *
 * @author mosesyu1028
 */
@Mixin(BlockSnow.class)
public class MixinBlockSnow {

	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		if (SnowReplaceableFlag.placingSnow) {
			return (world.getBlockMetadata(x, y, z) & 7) < 7;
		}
		return (world.getBlockMetadata(x, y, z) & 7) < 1;
	}
}
