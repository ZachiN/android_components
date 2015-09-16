package com.examples.components.censusapp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Zachi on 16/09/2015.
 */
public class AllContacts {
    private static AllContacts allContacts;
    private Context applicationContext;
    private List<Contact> contactList;

    private AllContacts(Context applicationContext) {
        this.applicationContext = applicationContext;
        contactList = new ArrayList<Contact>();

        Contact paulSmith = new Contact();
        paulSmith.setName("Paul Smith");
        paulSmith.setStreetAddress("123 Main Street");
        paulSmith.setContacted(true);
        contactList.add(paulSmith);

        Contact sallySmith = new Contact();
        sallySmith.setName("Sally Smith");
        sallySmith.setStreetAddress("125 Main St");
        sallySmith.setContacted(false);
        contactList.add(sallySmith);

        Contact markSmith = new Contact();
        markSmith.setName("Mark Smith");
        markSmith.setStreetAddress("127 Main St");
        markSmith.setContacted(false);
        contactList.add(markSmith);
    }

    public static AllContacts get(Context context) {
        if(allContacts == null) {
            allContacts = new AllContacts(context.getApplicationContext());
        }
        return allContacts;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public Contact getContact(UUID id) {
        for(Contact theContact : contactList) {
            if(theContact.getIdNumber().equals(id)) {
                return theContact;
            }
        }
        return null;
    }
}
