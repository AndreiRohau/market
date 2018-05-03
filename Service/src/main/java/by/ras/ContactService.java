package by.ras;

import by.ras.entity.particular.Contact;

import java.util.List;


public interface ContactService {

    Contact addContact(Contact contact);
    void delete(long id);
    Contact editContact(Contact contact);
    List<Contact> getAll();
}
