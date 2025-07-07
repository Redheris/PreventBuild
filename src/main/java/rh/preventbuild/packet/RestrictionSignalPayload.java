package rh.preventbuild.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RestrictionSignalPayload(String value) implements CustomPayload {
    public static final Identifier CHANNEL_ID = Identifier.of("preventbuild", "restrictions");
    public static final CustomPayload.Id<RestrictionSignalPayload> ID =
            new CustomPayload.Id<>(CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, RestrictionSignalPayload> CODEC =
        new PacketCodec<>() {
            @Override
            public RestrictionSignalPayload decode(PacketByteBuf buf) {
                String message = "";
                while (buf.readableBytes() > 0) {
                    byte[] bytes = buf.readByteArray();
                    message = new String(bytes);
                }
                return new RestrictionSignalPayload(message);
            }

            @Override
            public void encode(PacketByteBuf buf, RestrictionSignalPayload payload) {
                PacketCodecs.STRING.encode(buf, payload.value());
            }
        };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}