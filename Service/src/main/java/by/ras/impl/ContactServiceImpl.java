package by.ras.impl;

import by.ras.entity.particular.Contact;
import by.ras.exception.ServiceException;
import by.ras.repository.ContactRepository;
import by.ras.ContactService;
import by.ras.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
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
    public Contact findById(long id) throws ServiceException {
        try {
            return contactRepository.findOne(id);
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
            Contact dbContact = contactRepository.findOne(contact.getId());

            if(dbContact != null){
                dbContact.setCountry(contact.getCountry());
                dbContact.setCity(contact.getCity());
                dbContact.setStreet(contact.getStreet());
                dbContact.setHouseNumber(contact.getHouseNumber());
                dbContact.setPhoneNumber(contact.getPhoneNumber());
                contactRepository.saveAndFlush(dbContact);

            }
            return contact;
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