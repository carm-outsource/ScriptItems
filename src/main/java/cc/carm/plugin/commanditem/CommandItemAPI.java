package cc.carm.plugin.commanditem;

import cc.carm.plugin.commanditem.manager.ItemsManager;

public class CommandItemAPI {

    public static ItemsManager getItemsManager() {
        return Main.getInstance().itemsManager;
    }

}
