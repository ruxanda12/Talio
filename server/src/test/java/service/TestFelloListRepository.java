package service;

import commons.FelloList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.FelloListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestFelloListRepository implements FelloListRepository {

    public List<FelloList> felloLists = new ArrayList<>();

    @Override
    public List<FelloList> findAll() {
        return felloLists;
    }

    @Override
    public List<FelloList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<FelloList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<FelloList> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return felloLists.size();
    }

    @Override
    public void deleteById(Long aLong) {
        felloLists = felloLists
                .stream()
                .filter(l -> l.id != aLong)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(FelloList entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends FelloList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends FelloList> S save(S entity) {
        entity.id=felloLists.size();
        felloLists.add(entity);
        return entity;
    }

    @Override
    public <S extends FelloList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<FelloList> findById(Long aLong) {
        return felloLists.stream().filter(e -> e.id == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return felloLists.stream().anyMatch(e -> e.id == aLong);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends FelloList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends FelloList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<FelloList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public FelloList getOne(Long aLong) {
        return null;
    }

    @Override
    public FelloList getById(Long aLong) {
        var felloListOptional = felloLists.stream().filter(e -> e.id == aLong).findFirst();
        return felloListOptional.orElse(null);
    }

    @Override
    public <S extends FelloList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends FelloList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends FelloList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends FelloList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends FelloList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends FelloList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends FelloList, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}