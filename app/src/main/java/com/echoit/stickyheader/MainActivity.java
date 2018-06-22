package com.echoit.stickyheader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ContactVO> contactVOList = new ArrayList();
    AllContactsAdapter contactAdapter = null;
    private String contactID;
    ContactVO contactVO;
    String phoneNumber;
    public static final int REQUEST_READ_CONTACTS = 79;
    AllContactsAdapter allContactsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);

        allContactsAdapter = new AllContactsAdapter(MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(allContactsAdapter);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getAllContacts();
        } else {
            requestLocationPermission();

        }
    }

    protected void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                System.out.println("_______________ " + grantResults.length);

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("_______IF________ ");
                    getAllContacts();

                } else {

                    System.out.println("_______ELSE________ ");
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }

                break;

        }
    }

    private void getAllContacts() {


        contactVOList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactVO();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }


                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        contactVO.setContactEmail(emailId);
                    }
                    emailCursor.close();
                    //Image

                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                    Cursor Imagecursor = contentResolver.query(uri, new String[]{ContactsContract.PhoneLookup.PHOTO_URI}, null, null, null);
                    if (Imagecursor == null) {

                    }
                    String contactImage = null;
                    if (Imagecursor.moveToFirst()) {
                        contactImage = Imagecursor.getString(Imagecursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));
                        contactVO.setContactImage(contactImage);
                    }

                    if (!Imagecursor.isClosed()) {
                        Imagecursor.close();
                    }
                    contactVOList.add(contactVO);

                }
            }

        }
        if (contactVOList != null) {
            allContactsAdapter.addData(contactVOList);
            RecyclerSectionItemDecoration sectionItemDecoration =
                    new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                            true,
                            getSectionCallback(contactVOList));
            recyclerView.addItemDecoration(sectionItemDecoration);
        }
    }


    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<ContactVO> contactVOList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                return position == 0
                        || contactVOList.get(position)
                        .getContactName()
                        .charAt(0) == contactVOList.get(position - 1)
                        .getContactNumber()
                        .charAt(0);

            }

            @Override
            public CharSequence getSectionHeader(int position) {
                return contactVOList.get(position)
                        .getContactName()
                        .subSequence(0,
                                1);
            }
        };
    }
}