package edu.icbt.las.labappointmentsystem.service;


import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

  <S extends T> S save(S var1) throws ServiceException;

  <S extends T> Iterable<S> saveAll(Iterable<S> var1) throws ServiceException;

  Optional<T> findById(ID var1) throws ServiceException;

  boolean existsById(ID var1) throws ServiceException;

  List<T> findAll() throws ServiceException;

  List<T> findAllById(Iterable<ID> var1) throws ServiceException;

  long count() throws ServiceException;

  void deleteById(ID var1) throws ServiceException;

  void delete(T var1) throws ServiceException;

  void deleteAll(Iterable<? extends T> var1) throws ServiceException;

  void deleteAll() throws ServiceException;

  ServiceException translateException(DataAccessException de);
}
