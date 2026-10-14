package com.ruleta.colores.network;

import com.ruleta.colores.RuletaColores;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/** Servidor -> todos los clientes: muestra la notificacion de la ruleta. */
public record NotifyPayload(String color, String title, String message) implements CustomPayload {
    public static final Id<NotifyPayload> ID = new Id<>(RuletaColores.id("notify"));
    public static final PacketCodec<RegistryByteBuf, NotifyPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.string(32), NotifyPayload::color,
            PacketCodecs.string(128), NotifyPayload::title,
            PacketCodecs.string(512), NotifyPayload::message,
            NotifyPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
