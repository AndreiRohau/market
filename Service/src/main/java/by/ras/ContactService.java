package by.ras;

import by.ras.entity.particular.Contact;
import by.ras.exception.ServiceException;

import java.util.List;


public interface ContactService {

    Contact addContact(Contact contact) throws ServiceException;
    Contact findById(long id) throws ServiceException;
    List<Contact> getAll() throws ServiceException ;
    Contact editContact(Contact contact) throws ServiceException ;
    void delete(long id) throws ServiceException ;

}
