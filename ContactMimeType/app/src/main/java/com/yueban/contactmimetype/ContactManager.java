package com.yueban.contactmimetype;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2019-11-05
 */
public class ContactManager {
    private static final String ACCOUNT_NAME = "牛盾安全屋";

    private static final String ACCOUNT_TYPE = "com.yueban.contactmimetype";

    public static void addContact(Context context, String name, String phoneNumber) {
        Account account = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
        AccountManager.get(context).addAccountExplicitly(account, "", null);

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(
            ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, ACCOUNT_NAME)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, ACCOUNT_TYPE)
                //.withValue(RawContacts.SOURCE_ID, 12345)
//                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE,
//                        ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Settings.CONTENT_URI, true))
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, ACCOUNT_NAME)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, ACCOUNT_TYPE)
            .withValue(ContactsContract.Settings.UNGROUPED_VISIBLE, 1)
            .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.yueban.contactmimetype.android.message")
            .withValue(ContactsContract.Data.DATA1, phoneNumber)
            .withValue(ContactsContract.Data.DATA2, "密信: " + phoneNumber)
//            .withValue(ContactsContract.Data.DATA3, "密信: " + phoneNumber)
            .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.yueban.contactmimetype.android.call")
            .withValue(ContactsContract.Data.DATA1, phoneNumber)
            .withValue(ContactsContract.Data.DATA2, "密话: " + phoneNumber)
//            .withValue(ContactsContract.Data.DATA3, "密话: " + phoneNumber)
            .build());
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(context, "添加联系人成功", Toast.LENGTH_SHORT).show();
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void clearAll(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?",
            new String[] { ACCOUNT_TYPE });
        Toast.makeText(context, "清除联系人成功", Toast.LENGTH_SHORT).show();
    }

    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
        }
        return uri;
    }
}
