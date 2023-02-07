package cc.carm.plugin.scriptitems.manager;

import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import cc.carm.plugin.scriptitems.Main;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
import cc.carm.plugin.scriptitems.item.ScriptItem;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ItemsManager {

    private static final String FOLDER_NAME = "items";

    private HashMap<String, ScriptConfiguration> items = new HashMap<>();

    protected NamespacedKey idKey;
    protected NamespacedKey uuidKey;

    public void initialize() {
        this.idKey = new NamespacedKey(Main.getInstance(), "id");
        this.uuidKey = new NamespacedKey(Main.getInstance(), "uuid");
        loadItems();
        Main.info("成功加载了 " + items.size() + " 个脚本物品。");
    }

    public void loadItems() {
        File prefixDataFolder = getStorageFolder();
        if (!prefixDataFolder.isDirectory() || !prefixDataFolder.exists()) {
            try {
                JarResourceUtils.copyFolderFromJar(
                        FOLDER_NAME, Main.getInstance().getDataFolder(),
                        JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST
                );
            } catch (Exception ex) {
                boolean success = prefixDataFolder.mkdirs();
            }
        }

        String[] filesList = prefixDataFolder.list();
        if (filesList == null || filesList.length < 1) {
            Main.severe("   配置文件夹中暂无任何物品，请检查。");
            Main.severe("   There's no configured items.");
            return;
        }

        List<File> files = Arrays.stream(filesList)
                .map(s -> new File(prefixDataFolder, s))
                .filter(File::isFile)
                .collect(Collectors.toList());

        HashMap<String, ScriptConfiguration> dataItems = new HashMap<>();

        if (files.size() > 0) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith(".")) continue;
                try {
                    ScriptConfiguration item = ScriptConfiguration.load(file);
                    Main.info(" 完成脚本物品加载 [#" + item.getIdentifier() + "] " + item.getName() + " (" + fileName + ")");
                    dataItems.put(item.getIdentifier(), item);
                } catch (Exception ex) {
                    Main.severe("在加载脚本物品 " + file.getAbsolutePath() + " 时出错，请检查配置！");
                    Main.severe("Error occurred when loading item #" + file.getAbsolutePath() + " !");
                    ex.printStackTrace();
                }
            }
        }

        items = dataItems;
    }

    private static File getStorageFolder() {
        return new File(Main.getInstance().getDataFolder(), FOLDER_NAME);
    }

    @Unmodifiable
    public Map<String, ScriptConfiguration> listItemSettings() {
        return ImmutableMap.copyOf(items);
    }

    public @Nullable ScriptConfiguration getItemSettings(@NotNull String identifier) {
        return items.get(identifier);
    }

    public ItemStack applyTag(@NotNull ItemStack originalItem, String identifier, UUID uuid) {
        ItemMeta meta = originalItem.getItemMeta();
        if (meta == null) return originalItem;
        CustomItemTagContainer container = meta.getCustomTagContainer();
        container.setCustomTag(idKey, ItemTagType.STRING, identifier);
        container.setCustomTag(uuidKey, ItemTagType.STRING, uuid.toString());
        originalItem.setItemMeta(meta);
        return originalItem;
    }

    public @Nullable ScriptItem parseItem(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        CustomItemTagContainer container = meta.getCustomTagContainer();
        String settingsID = container.getCustomTag(this.idKey, ItemTagType.STRING);
        String itemUUID = container.getCustomTag(this.uuidKey, ItemTagType.STRING);
        if (settingsID == null || itemUUID == null) return null;
        ScriptConfiguration settings = getItemSettings(settingsID);
        if (settings == null) return null;
        return new ScriptItem(UUID.fromString(itemUUID), settings, item);
    }

    public boolean isScriptItem(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta() != null
                && item.getItemMeta().getCustomTagContainer().hasCustomTag(idKey, ItemTagType.STRING);
    }


}
