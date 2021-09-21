package ru.irlix.evaluation.utils.localization;

import ru.irlix.evaluation.config.UTF8Control;
import ru.irlix.evaluation.utils.constant.LocaleConstants;

import java.util.ResourceBundle;

public class MessageBundle {

    public static ResourceBundle getMessageBundle() {
        return ResourceBundle.getBundle(
                "messages",
                LocaleConstants.DEFAULT_LOCALE,
                new UTF8Control()
        );
    }
}
