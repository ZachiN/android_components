package com.examples.components.censusapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.examples.components.censusapp.R;
import com.examples.components.censusapp.models.Contact;

/**
 * Created by Zachi on 05/09/2015.
 */
public class ContactFragment extends Fragment {

    private Contact contact;
    private EditText contactNameEditText;

    private EditText contactStreetEditText;
    private EditText contactCityEditText;
    private EditText contactPhoneEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contact = new Contact();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_contact, container, false);

        contactNameEditText = (EditText) theView.findViewById(R.id.contactNameEditText);
        contactStreetEditText = (EditText) theView.findViewById(R.id.contactStreetEditText);
        contactCityEditText = (EditText) theView.findViewById(R.id.contactCityEditText);
        contactPhoneEditText = (EditText) theView.findViewById(R.id.contactPhoneEditText);

        TextWatcher tw = new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(contactNameEditText.hasFocus()) {
                    contact.setName(s.toString());
                } else if(contactStreetEditText.hasFocus()) {
                    contact.setStreetAddress(s.toString());
                } else if(contactCityEditText.hasFocus()) {
                    contact.setCity(s.toString());
                } else if(contactPhoneEditText.hasFocus()) {
                    contact.setPhoneNumber(s.toString());
                }
            }
        };

        contactNameEditText.addTextChangedListener(tw);
        contactStreetEditText.addTextChangedListener(tw);
        contactCityEditText.addTextChangedListener(tw);
        contactPhoneEditText.addTextChangedListener(tw);

        return theView;
    }
}
