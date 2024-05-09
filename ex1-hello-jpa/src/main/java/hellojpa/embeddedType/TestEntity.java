package hellojpa.embeddedType;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class TestEntity {
    private Long id;
    private String name;

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity testEntity = (TestEntity) o;
        return Objects.equals(id, testEntity.id) &&
                Objects.equals(name, testEntity.name);

    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
