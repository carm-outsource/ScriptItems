package cc.carm.plugin.scriptitems.item;

public enum ItemExecuteResult {

    SUCCESS(0),
    FAILED(1),

    ;

    int id;

    ItemExecuteResult(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
