package news.caughtup.caughtup.entities;

public abstract class Resource {

    String name;

    public Resource(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
