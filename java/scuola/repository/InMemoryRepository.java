package scuola.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Bazowe repozytorium przechowujące encje w pamięci.
 *
 * <p>Zastępuje warstwę bazy danych z diagramów sekwencji: zapytania typu
 * „pobierz wszystkie X" realizujemy jako {@link #findAll()}, a konkretne
 * repozytoria dokładają wyszukiwania dziedzinowe (np. {@code findByTeacher}).</p>
 *
 * @param <T> typ przechowywanej encji
 */
public abstract class InMemoryRepository<T> {

    protected final List<T> items = new ArrayList<>();

    public T save(T item) {
        if (item != null && !items.contains(item)) {
            items.add(item);
        }
        return item;
    }

    public boolean delete(T item) {
        return items.remove(item);
    }

    public List<T> findAll() {
        return List.copyOf(items);
    }

    public int count() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}
