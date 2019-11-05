package com.yueban.contactmimetype;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
        }
        return uri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[] { Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                final String accountName = "芯盾";
                final String accountType = "com.yueban.contactmimetype";

                ContentResolver resolver = getContentResolver();
                resolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?",
                    new String[] { accountType });

                Account account = new Account(accountName, accountType);
                AccountManager.get(this).addAccountExplicitly(account, "", null);

                ArrayList<ContentProviderOperation> ops = new ArrayList<>();

                ops.add(ContentProviderOperation.newInsert(
                    addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
                    //.withValue(RawContacts.SOURCE_ID, 12345)
                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE,
                        ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                    .build());

                ops.add(ContentProviderOperation.newInsert(
                    addCallerIsSyncAdapterParameter(ContactsContract.Settings.CONTENT_URI, true))
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
                    .withValue(ContactsContract.Settings.UNGROUPED_VISIBLE, 1)
                    .build());

                ops.add(
                    ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "明")
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "罗")
                        .build());

//                ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI,
//                true))
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "13822294485")
//                    .build());

//                ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI,
//                true))
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, "sample@email.com")
//                    .build());

                ops.add(
                    ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            "vnd.android.cursor.item/vnd.com.yueban.contactmimetype.android.profile")
                        .withValue(ContactsContract.Data.DATA1, 12345)
                        .withValue(ContactsContract.Data.DATA2, "自定义信息")
                        .withValue(ContactsContract.Data.DATA3, "13822294485")
                        .build());

                ops.add(
                    ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            "vnd.android.cursor.item/vnd.com.yueban.contactmimetype.android.call")
                        .withValue(ContactsContract.Data.DATA1, 12345)
                        .withValue(ContactsContract.Data.DATA2, "自定义信息2")
                        .withValue(ContactsContract.Data.DATA3, "13822294485___")
                        .build());

//                ContentProviderOperation query = ContentProviderOperation.newInsert(
//                    ContactsContract.RawContacts.CONTENT_URI.buildUpon()
//                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
//                        .build())
//                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "芯盾")
//                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "com.yueban.contactmimetype")
//                    .withValue(ContactsContract.RawContacts.SYNC1, "13822294485")
//                    .withValue(ContactsContract.RawContacts.SYNC2, "10001")
//                    .build();
//
//                queries.add(query);
//
//                query = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName
//                    .CONTENT_ITEM_TYPE)
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, user.first_name)
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, user.last_name)
//                    .build();
//
//                queries.add(query);
//
//                query = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                        "`vnd.android.cursor.item/vnd.com.yueban.contactmimetype.android.profile`")
//                    .withValue(ContactsContract.Data.DATA1, "10001")
//                    .withValue(ContactsContract.Data.DATA2, "安全屋信息")
//                    .withValue(ContactsContract.Data.DATA3, "13822294485")
//                    .withValue(ContactsContract.Data.DATA4, "10001")
//                    .build();
//
//                queries.add(query);
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (OperationApplicationException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
