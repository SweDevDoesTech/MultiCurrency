package dev.sweplays.multicurrency.files;

import lombok.Getter;

public class FileManager {

    @Getter
    private final MessagesFile messagesFile;

    public FileManager() {
        messagesFile = new MessagesFile();
    }
}
