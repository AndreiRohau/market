package by.ras.impl;

import by.ras.entity.particular.Contact;
import by.ras.exception.ServiceException;
import by.ras.repository.ContactRepository;
import by.ras.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    @Override
    public Contact addContact(Contact contact) throws ServiceException {
        try {
            return contactRepository.saveAndFlush(contact);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            contactRepository.delete(id);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Contact editContact(Contact contact) throws ServiceException {
        try {
            return contactRepository.saveAndFlush(contact);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Contact> getAll() throws ServiceException {
        try {
            return contactRepository.findAll();
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}