package cc.happyareabean.swmhook.hook.impl;

import cc.happyareabean.swmhook.event.SWMWorldLoadedEvent;
import cc.happyareabean.swmhook.hook.HookAdapter;
import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.CorruptedWorldException;
import com.infernalsuite.asp.api.exceptions.NewerFormatException;
import com.infernalsuite.asp.api.exceptions.UnknownWorldException;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimeProperties;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.infernalsuite.asp.loaders.file.FileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SlimeWorldPluginHookAdapter extends HookAdapter {

    private final FileLoader fileLoader;
    private final Logger log = LogManager.getLogger();
    private final AdvancedSlimePaperAPI asp = AdvancedSlimePaperAPI.instance();

    public SlimeWorldPluginHookAdapter(JavaPlugin plugin) {
        super(plugin);
        fileLoader = new FileLoader(new File(plugin.getDataFolder(), "slimehook_worlds"));
    }

    @Override
    public boolean isLoaderValid(String loader) {
        return fileLoader != null;
    }

    @Override
    public boolean isWorldExist(String worldName, String loader) {
        return fileLoader.worldExists(worldName);
    }

    @Override
    public boolean isSlimeWorld(World world) {
        return asp.getLoadedWorld(world.getName()) != null;
    }

    @Override
    public void loadWorld(String templateWorldName, String worldName, String loaderName) {
        try {
            long start = System.currentTimeMillis();

            if (fileLoader == null) {
                log.error("Invalid data source {}", loaderName);
            }

            CompletableFuture.runAsync(() -> {
                try {
                    SlimeWorld world = asp.getLoadedWorld(templateWorldName) == null ?
                            asp.readWorld(fileLoader, templateWorldName, false, createPropertyMap(0, 100, 0)) :
                            asp.getLoadedWorld(templateWorldName);
                    SlimeWorld slimeWorld = world.clone(worldName);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        asp.loadWorld(slimeWorld, true);

                        log.info("World {} loaded and generated in {}ms!", worldName, System.currentTimeMillis() - start);
                        Bukkit.getPluginManager().callEvent(new SWMWorldLoadedEvent(templateWorldName, worldName, true));
                    });
                } catch (IOException | UnknownWorldException | CorruptedWorldException | NewerFormatException ex) {
                    log.error("Failed to generate world {}: {}.", worldName, ex.getMessage(), ex);
                }
            });
        } catch (Throwable ex) {
            log.error("Failed to generate world {}: {}.", worldName, ex.getMessage(), ex);
        }
    }

    @Override
    public String getAdapterName() {
        return "Slime World Plugin (SWP)";
    }

    @Override
    public String getPluginName() {
        return "SlimeWorldPlugin";
    }

    @Override
    public String getProviderAuthor() {
        return "HappyAreaBean";
    }

    private SlimePropertyMap createPropertyMap(int spawnX, int spawnY, int spawnZ) {
        SlimePropertyMap propertyMap = new SlimePropertyMap();
        propertyMap.setValue(SlimeProperties.WORLD_TYPE, "flat");
        propertyMap.setValue(SlimeProperties.SPAWN_X, spawnX);
        propertyMap.setValue(SlimeProperties.SPAWN_Y, spawnY);
        propertyMap.setValue(SlimeProperties.SPAWN_Z, spawnZ);
        propertyMap.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        propertyMap.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        propertyMap.setValue(SlimeProperties.DIFFICULTY, "easy");
        propertyMap.setValue(SlimeProperties.PVP, true);
        return propertyMap;
    }
}
