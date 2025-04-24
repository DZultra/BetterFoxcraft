package net.dzultra.betterfoxcraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.nio.file.Path;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {
    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void addScreenshotButton(CallbackInfo ci) {
        ScreenAccessor accessor = (ScreenAccessor)(Object)this;
        accessor.invokeAddDrawableChild(ButtonWidget.builder(
                Text.literal("Screenshots"),
                button -> openScreenshotFolder()
        ).dimensions(5, 5, 80, 20).build());
    }

    @Unique
    private void openScreenshotFolder() {
        try {
            Path screenshotsDir = MinecraftClient.getInstance().runDirectory.toPath().resolve("screenshots");
            Util.getOperatingSystem().open(screenshotsDir.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
