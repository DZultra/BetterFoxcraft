package net.dzultra.betterfoxcraft.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static KeyBinding openConfigKeybind;

    public static void register() {
        openConfigKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterfoxcraft.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "keybinds.betterfoxcraft.category"
        ));
    }
}
