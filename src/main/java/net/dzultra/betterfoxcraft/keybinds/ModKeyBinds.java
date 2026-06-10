package net.dzultra.betterfoxcraft.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static KeyMapping openConfigKeybind;
    public static KeyMapping moveKeybind;
    private static final KeyMapping.Category BetterFoxcraftCategory = new KeyMapping.Category(Identifier.parse("keybinds.betterfoxcraft.category"));


    public static void register() {
        openConfigKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.betterfoxcraft.open_settings",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                BetterFoxcraftCategory
        ));
        moveKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.betterfoxcraft.move",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                BetterFoxcraftCategory
        ));
    }
}
