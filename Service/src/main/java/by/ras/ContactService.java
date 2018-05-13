package by.ras;

import by.ras.entity.particular.Contact;
import by.ras.exception.ServiceException;

public interface ContactService {

    Contact addContact(Contact contact) throws ServiceException;

    Contact findById(long id) throws ServiceException;

    Contact editContact(Contact contact) throws ServiceException ;

}
