package com.semivanilla.bounties.utils.utility;

import com.semivanilla.bounties.utils.UtilityManager;
import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

public class FileUtility {

    private final UtilityManager manager;

    public FileUtility(UtilityManager manager) {
        this.manager = manager;
    }

    public InputStream getResourceAsStream(@NotNull String name){
        return manager.getJavaPlugin().getResource(name);
    }

    public FileUtility generateParentFolder(){
        if(manager.getJavaPlugin().getDataFolder().exists())
            return this;
        manager.getJavaPlugin().getDataFolder().mkdirs();
        return this;
    }

    public Json createJson(@NotNull String fileName){
        return new Json(fileName,manager.getJavaPlugin().getDataFolder().getPath()+ File.separator+"data-storage");
    }

    public void executeAsyncIfExists(@NotNull String fileName, Consumer<File> toPerform){
        File file = new File(manager.getJavaPlugin().getDataFolder(),fileName);
        if(file.exists()){
            manager.getJavaPlugin().getServer().getScheduler().runTaskAsynchronously(manager.getJavaPlugin(), new Runnable() {
                @Override
                public void run() {
                    toPerform.accept(file);
                }
            });
        }
    }

    public void executeIfExists(@NotNull String fileName, Consumer<File> toPerform){
        File file = new File(manager.getJavaPlugin().getDataFolder(),fileName);
        if(file.exists()){
            toPerform.accept(file);
        }
    }
}
