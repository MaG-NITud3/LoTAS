package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@Mixin(MinecraftServer.class)
public abstract class BindMinecraftServer {
	
	@Shadow
	public WorldServer[] worldServers;
	
	@Shadow
	public boolean serverRunning;
	
	/*
	 * This Part synchronizes the Server to the Client
	 */
	
	@ModifyVariable(method = "run", at = @At("STORE"), index = 3, ordinal = 1)
	public long undoServerTicking(long in) {
		return 0;
	}
	
	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
	public void redoServerWait(long millis) throws InterruptedException {
		//Applying tickrate on the server side
		this.tick();
		
		if (TickrateChanger.tickrateServer == 0) {
			//Thread sleeping until the tickrate is something different than 0 or ticksync is correct again
			while (TickrateChanger.tickrateServer == 0) {
				Thread.sleep(1);
			}
		} else {
			Thread.sleep(Math.max(1L, (1000 / TickrateChanger.tickrateServer)));
		}
		TickrateChanger.ticksPassedServer++;
		TickrateChanger.resetAdvanceServer();
		
		// fuck you
		if (worldServers == null) serverRunning = false;
		else if (worldServers.length == 0) serverRunning = false;
	}
	
	@Shadow
	protected abstract void tick();
	
}
