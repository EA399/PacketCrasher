package com.example.addon.modules;

import com.example.addon.PacketAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.SwingAnimation;

public class PacketBurst extends Module {
    public enum Mode {
        Swing,      // Arm-Swing-Pakete
        Position    // Positions-Pakete an der aktuellen Stelle
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Welche Art von Paket gesendet wird.")
        .defaultValue(Mode.Swing)
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("packets")
        .description("Anzahl der Pakete pro Burst (1-50).")
        .defaultValue(10)
        .min(1)
        .max(50)
        .sliderRange(1, 50)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Ticks zwischen zwei Bursts.")
        .defaultValue(20)
        .min(1)
        .sliderRange(1, 100)
        .build()
    );

    private final Setting<Boolean> once = sgGeneral.add(new BoolSetting.Builder()
        .name("once")
        .description("Sendet nur einen Burst und schaltet sich dann aus.")
        .defaultValue(false)
        .build()
    );

    private int timer = 0;

    public PacketBurst() {
        super(PacketAddon.CATEGORY, "packet-burst", "Sendet 1-50 Pakete auf einmal.");
    }

    @Override
    public void onActivate() {
        timer = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;

        if (timer > 0) {
            timer--;
            return;
        }
        timer = delay.get();

        for (int i = 0; i < amount.get(); i++) {
            switch (mode.get()) {
                case Swing -> mc.player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, true);
                case Position -> mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    true, false));
            }
        }

        if (once.get()) toggle();
    }

    @Override
    public String getInfoString() {
        return amount.get() + "x " + mode.get();
    }
}
