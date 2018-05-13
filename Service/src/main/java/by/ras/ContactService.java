package by.ras;

import by.ras.entity.particular.Contact;
import by.ras.exception.ServiceException;

import java.util.List;

public interface ContactService {

    Contact addContact(Contact contact) throws ServiceException;

    Contact editContact(Contact contact) throws ServiceException ;

}
