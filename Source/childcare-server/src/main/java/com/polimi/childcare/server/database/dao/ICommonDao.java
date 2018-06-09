package com.polimi.childcare.server.database.dao;

public interface ICommonDao<T>
{
    void delete(T gruppo);
    int insert(T gruppo);
    void update(T gruppo);
}
