package net.fabricmc.example.mixin.invoker;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */

@Mixin(ClientConnection.class)
public interface ClientConnectionInvoker {
    @Invoker void callSendImmediately(Packet<?> packet, PacketCallbacks callbacks);

}

