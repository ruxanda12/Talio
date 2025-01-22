//package server.api;
//
//import commons.Task;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import server.database.TaskRepository;
//
//import java.util.*;
//import java.util.function.Function;
//
//public class TestTaskRepository implements TaskRepository {
//
//    public Set<Task> tasks = new HashSet<Task>();
//
//    @Override
//    public List<Task> findAll() {
//        return List.copyOf(tasks);
//    }
//
//    @Override
//    public List<Task> findAll(Sort sort) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public Page<Task> findAll(Pageable pageable) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public List<Task> findAllById(Iterable<Long> longs) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public long count() {
//        return tasks.size();
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//        tasks.removeIf(task -> task.getId() == (aLong));
//    }
//
//    @Override
//    public void delete(Task entity) {
//        tasks.remove(entity);
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Task> entities) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void deleteAll() {
//        tasks.clear();
//    }
//
//    @Override
//    public <S extends Task> S save(S entity) {
//        tasks.add(entity);
//        return entity;
//    }
//
//    @Override
//    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
//        entities.forEach(this::save);
//        return (List<S>) List.copyOf(tasks);
//    }
//
//    @Override
//    public Optional<Task> findById(Long aLong) {
//        return tasks.stream().filter(task -> task.getId() == (aLong)).findFirst();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return tasks.stream().anyMatch(task -> task.getId() == (aLong));
//    }
//
//    @Override
//    public void flush() {
//        // No-op
//    }
//
//    @Override
//    public <S extends Task> S saveAndFlush(S entity) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Task> entities) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public Task getOne(Long aLong) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public Task getById(Long aLong) {
//        return findById(aLong).orElse(null);
//    }
//
//    @Override
//    public <S extends Task> Optional<S> findOne(Example<S> example) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public <S extends Task> List<S> findAll(Example<S> example) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * @param example  must not be {@literal null}.
//     * @param pageable can be {@literal null}.
//     * @param <S>
//     * @return
//     */
//    @Override
//    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
//     * @param <S>
//     * @return
//     */
//    @Override
//    public <S extends Task> long count(Example<S> example) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
//     * @param <S>
//     * @return
//     */
//    @Override
//    public <S extends Task> boolean exists(Example<S> example) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * @param example       must not be {@literal null}.
//     * @param queryFunction the query function defining projection, sorting, and the result type
//     * @param <S>
//     * @param <R>
//     * @return
//     */
//    @Override
//    public <S extends Task, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        throw new UnsupportedOperationException();
//    }
//}
