package com.ruleta.colores.client;

import com.ruleta.colores.network.DeathPayload;
import com.ruleta.colores.network.NotifyPayload;
import com.ruleta.colores.network.OpenMenuPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class RuletaColoresClient implements ClientModInitializer {
    private boolean lastLDown = false;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenMenuPayload.ID, (payload, context) ->
                context.client().setScreen(new RuletaMenuScreen()));

        ClientPlayNetworking.registerGlobalReceiver(NotifyPayload.ID, (payload, context) ->
                NotificationHud.show(payload.color(), payload.title(), payload.message()));

        ClientPlayNetworking.registerGlobalReceiver(DeathPayload.ID, (payload, context) ->
                DeathAnimation.start(payload.playerName(), payload.key(), payload.args(), payload.fallback()));

        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            NotificationHud.render(ctx);
            DeathAnimation.render(ctx, tickCounter);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            DeathAnimation.tick();
            // Tecla L: ocultar la notificacion (solo con la notificacion visible y sin pantallas abiertas)
            boolean down = client.currentScreen == null
                    && InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_L);
            if (down && !lastLDown && NotificationHud.isVisible()) {
                NotificationHud.hide();
            }
            lastLDown = down;
        });
    }
}
