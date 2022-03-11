package cc.carm.plugin.commanditem.manager;

import cc.carm.plugin.commanditem.Main;
import cc.carm.plugin.commanditem.configuration.PluginConfig;
import cc.carm.plugin.commanditem.item.CommandItem;
import cc.carm.plugin.commanditem.item.ItemSettings;
import com.google.common.collect.ImmutableMap;
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

    public HashMap<String, ItemSettings> items = new HashMap<>();

    protected NamespacedKey idKey;
    protected NamespacedKey uuidKey;

    public void initialize() {
        this.idKey = new NamespacedKey(Main.getInstance(), "id");
        this.uuidKey = new NamespacedKey(Main.getInstance(), "uuid");
        loadItems();
        Main.info("共加载了 " + items.size() + " 个前缀。");
    }

    public void loadItems() {
        File prefixDataFolder = getStorageFolder();
        if (!prefixDataFolder.isDirectory() || !prefixDataFolder.exists()) {
            boolean success = prefixDataFolder.mkdir();
        }

        String[] filesList = prefixDataFolder.list();
        if (filesList == null || filesList.length < 1) {
            Main.severe("配置文件夹中暂无任何物品，请检查。");
            Main.severe("There's no configured items.");
            Main.severe("Path: " + prefixDataFolder.getAbsolutePath());
            return;
        }

        List<File> files = Arrays.stream(filesList)
                .map(s -> new File(prefixDataFolder, s))
                .filter(File::isFile)
                .collect(Collectors.toList());

        HashMap<String, ItemSettings> dataItems = new HashMap<>();

        if (files.size() > 0) {
            for (File file : files) {
                try {
                    ItemSettings item = ItemSettings.load(file);
                    Main.info("完成物品加载 " + item.getIdentifier() + " : " + item.getName());
                    Main.info("Successfully loaded " + item.getIdentifier() + " : " + item.getName());
                    dataItems.put(item.getIdentifier(), item);
                } catch (Exception ex) {
                    Main.severe("在加载物品 " + file.getAbsolutePath() + " 时出错，请检查配置！");
                    Main.severe("Error occurred when loading item #" + file.getAbsolutePath() + " !");
                    ex.printStackTrace();
                }
            }
        }

        items = dataItems;
    }

    private static File getStorageFolder() {
        if (PluginConfig.CustomStorage.ENABLE.get()) {
            return new File(PluginConfig.CustomStorage.PATH.get());
        } else {
            return new File(Main.getInstance().getDataFolder() + File.separator + FOLDER_NAME);
        }
    }

    @Unmodifiable
    public Map<String, ItemSettings> listItemSettings() {
        return ImmutableMap.copyOf(items);
    }

    public @Nullable ItemSettings getItemSettings(@NotNull String identifier) {
        return items.get(identifier);
    }

    public ItemStack applyTag(@NotNull ItemStack originalItem, String identifier, UUID uuid) {
        if (!originalItem.hasItemMeta()) return originalItem;
        ItemMeta meta = originalItem.getItemMeta();
        if (meta == null) return originalItem;
        CustomItemTagContainer container = meta.getCustomTagContainer();
        container.setCustomTag(idKey, ItemTagType.STRING, identifier);
        container.setCustomTag(uuidKey, ItemTagType.STRING, uuid.toString());
        originalItem.setItemMeta(meta);
        return originalItem;
    }

    public @Nullable CommandItem parseCommandItem(@Nullable ItemStack item) {
        if (item == null) return null;
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        CustomItemTagContainer container = meta.getCustomTagContainer();
        String settingsID = container.getCustomTag(this.idKey, ItemTagType.STRING);
        String itemUUID = container.getCustomTag(this.uuidKey, ItemTagType.STRING);
        if (settingsID == null || itemUUID == null) return null;
        ItemSettings settings = getItemSettings(settingsID);
        if (settings == null) return null;
        return new CommandItem(UUID.fromString(itemUUID), settings, item);
    }

    public boolean isCommandItem(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta() != null
                && item.getItemMeta().getCustomTagContainer().hasCustomTag(idKey, ItemTagType.STRING);
    }


}
