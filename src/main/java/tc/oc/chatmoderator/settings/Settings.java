package tc.oc.chatmoderator.settings;

import me.anxuiz.settings.Setting;
import me.anxuiz.settings.SettingBuilder;
import me.anxuiz.settings.SettingRegistry;
import me.anxuiz.settings.bukkit.PlayerSettings;
import me.anxuiz.settings.types.EnumType;

public class Settings {

    public static void register() {
        SettingRegistry register = PlayerSettings.getRegistry();
        if (!register.isRegistered(FILTER_SETTING)) {
            register.register(FILTER_SETTING);
        }
    }

    public static final Setting FILTER_SETTING = new SettingBuilder()
        .name("ChatModerator").alias("filters").alias("filter").alias("cm")
        .summary("Detail the level of filters you want run on chat")
        .description("ALL: All messages containing profanity will not be received\n" +
                     "OFFENSIVE: Any message containing offensive profanity won't be received\n" +
                     "NONE: All messages will be received")
        .type(new EnumType<FilterOptions>("Filter Options", FilterOptions.class))
        .defaultValue(FilterOptions.OFFENSIVE).get();
}
