package news.caughtup.caughtup.entities;

public abstract class Resource {

    String name;
    int resourceId;

    public Resource(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public int getResourceId() {
        return resourceId;
    }
}
