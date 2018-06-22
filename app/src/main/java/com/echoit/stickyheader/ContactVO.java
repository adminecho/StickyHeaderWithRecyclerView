package com.echoit.stickyheader;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by EchoIT on 6/20/2018.
 */

public class ContactVO implements Serializable{
    private String ContactImage = "";
    private String ContactName = "";
    private String ContactNumber = "";
    private String ContactEmail = "";

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        ContactImage = contactImage;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        ContactEmail = contactEmail;
    }




    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
