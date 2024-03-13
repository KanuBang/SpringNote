package hello.core.singleton;

public class StatefulSearchService {
    private String record;
    public void search(String name, String record) {
        System.out.println("name: " + name + " record: " + record);
        this.record = record;
    }
    public String  getrecord() {
        return record;
    }
}
