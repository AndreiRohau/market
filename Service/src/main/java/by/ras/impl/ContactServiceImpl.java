package by.ras.impl;

import by.ras.entity.particular.Contact;
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
    public Contact addContact(Contact contact) {
        System.out.println(contactRepository);
        return contactRepository.saveAndFlush(contact);
    }

    @Override
    public void delete(long id) {
        contactRepository.delete(id);
    }

    @Override
    public Contact editContact(Contact contact) {
        return contactRepository.saveAndFlush(contact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }
}