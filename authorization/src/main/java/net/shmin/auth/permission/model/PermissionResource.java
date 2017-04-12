package net.shmin.auth.permission.model;

/**
 * Created by benjamin on 2017/1/4.
 */
public class PermissionResource<T> {

    private T id;

    private String name;

    /**
     * MENU, BUTTON, DATA
     */
    private String type;

    /**
     * CREATE,UPDATE,RETRIEVE,DELETE[,A,B,C,D,E,CUSTOM]
     */
    private String availableOperations;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableOperation() {
        return availableOperations;
    }

    public void setAvailableOperation(String availableOperation) {
        this.availableOperations = availableOperation;
    }
}
