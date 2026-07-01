package com.example.tourplannerbackend.mapper;

public abstract class AbstractMapper<E, D> {
    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);
}