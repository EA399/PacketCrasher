package com.example.addon;

import com.example.addon.modules.PacketBurst;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketAddon extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger("PacketAddon");
    public static final Category CATEGORY = new Category("Packets");

    @Override
    public void onInitialize() {
        LOG.info("Packet Addon geladen");
        Modules.get().add(new PacketBurst());
    }

    @Override
    public void onRegisterCategories() {
        Modules.get().registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}
