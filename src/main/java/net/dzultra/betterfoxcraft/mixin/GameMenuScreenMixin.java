package net.dzultra.betterfoxcraft.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.nio.file.Path;

@Mixin(PauseScreen.class)
public class GameMenuScreenMixin {
    @Inject(method = "createPauseMenu", at = @At("TAIL"))
    private void addScreenshotButton(CallbackInfo ci) {
        ScreenAccessor accessor = (ScreenAccessor)(Object)this;
        accessor.invokeAddDrawableChild(Button.builder(
                Component.literal("Screenshots"),
                button -> openScreenshotFolder()
        ).bounds(5, 5, 80, 20).build());
    }

    @Unique
    private void openScreenshotFolder() {
        try {
            Path screenshotsDir = Minecraft.getInstance().gameDirectory.toPath().resolve("screenshots");
            Util.getPlatform().openFile(screenshotsDir.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
