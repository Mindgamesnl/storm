package com.craftmend.storm.parser.types.relations;

import com.craftmend.storm.api.StormModel;

import java.util.Collection;
import java.util.HashSet;

public class ManyToOne<T extends StormModel> extends HashSet<T> {

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            add(t);
        }
        return true;
    }

    @Override
    public boolean add(T c) {
        return super.add(c);
    }

    @Override
    public boolean remove(Object c) {
        return super.remove(c);
    }

    @Override
    public void clear() {
        super.clear();
    }

}
