package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author Pancake
 *
 */
@Mixin(EntityPlayerMP.class)
public interface AccessEPMP {

	@Accessor("field_147101_bU")
	public int getfield_147101_bU();
	
	@Accessor("field_147101_bU")
	public void setfield_147101_bU(int i);
	
	
}
