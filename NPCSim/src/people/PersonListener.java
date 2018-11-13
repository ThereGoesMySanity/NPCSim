package people;

public interface PersonListener {
    void onChild(Person p, Person child);
    void onMarry(Person p, Person spouse);
    void onDeath(Person p);
}
