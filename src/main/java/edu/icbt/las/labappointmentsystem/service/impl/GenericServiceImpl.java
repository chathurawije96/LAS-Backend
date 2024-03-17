package edu.icbt.las.labappointmentsystem.service.impl;


import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.service.GenericService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED)
public class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID> {

  protected CrudRepository<T, ID> abstractDao;
  private Class<? extends T> type;
  private static Validator validator;
  private static ValidatorFactory factory;


  protected GenericServiceImpl() {
    if (factory == null) {
      factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();
    }
  }

  protected void init(CrudRepository<T, ID> dao) {
    this.abstractDao = dao;
  }

  protected void init(Class<? extends T> type, CrudRepository<T, ID> dao) {
    this.type = type;
    this.abstractDao = dao;
  }


  @Override
  public <S extends T> S save(S var1) throws ServiceException {
    return abstractDao.save(var1);
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> var1) throws ServiceException {
    return abstractDao.saveAll(var1);
  }

  @Override
  public Optional<T> findById(ID var1) throws ServiceException {
    return abstractDao.findById(var1);
  }

  @Override
  public boolean existsById(ID var1) throws ServiceException {
    return abstractDao.existsById(var1);
  }

  @Override
  public List<T> findAll() throws ServiceException {
    List<T> list = new ArrayList<>();
    abstractDao.findAll().forEach(list::add);
    return list;
  }

  @Override
  public List<T> findAllById(Iterable<ID> var1) throws ServiceException {
    List<T> list = new ArrayList<>();
    abstractDao.findAllById(var1).forEach(list::add);
    return list;
  }

  @Override
  public long count() throws ServiceException {
    return abstractDao.count();
  }

  @Override
  public void deleteById(ID var1) throws ServiceException {
    abstractDao.deleteById(var1);
  }

  @Override
  public void delete(T var1) throws ServiceException {
    abstractDao.delete(var1);
  }

  @Override
  public void deleteAll(Iterable<? extends T> var1) throws ServiceException {
    abstractDao.deleteAll(var1);
  }

  @Override
  public void deleteAll() throws ServiceException {
    abstractDao.deleteAll();
  }

  @Override
  public ServiceException translateException(DataAccessException de) {
    switch (de.getCode()) {
      case DataAccessException.VALIDATION_FAILED:
        return new ServiceException(ServiceException.VALIDATION_FAILED, de.getMessage(), de);
      default:
        return new ServiceException(ServiceException.PROCESSING_FAILED, de.getMessage(), de);
    }
  }
}
