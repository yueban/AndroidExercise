package com.juphoon.cloud.sample;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.juphoon.cloud.JCClient;
import com.juphoon.cloud.JCLog;
import com.juphoon.cloud.JCMediaChannel;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;
import com.justalk.cloud.lemon.MtcConf;
import com.justalk.cloud.lemon.MtcMedia;
import com.justalk.cloud.lemon.MtcVer;
import com.justalk.cloud.zmf.Zmf;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            if (setToSdkIfNeed(preference, value)) {
                if (preference instanceof EditTextPreference) {
                    preference.setSummary((String) value);
                } else if (preference instanceof ListPreference) {
                    preference.setSummary(((ListPreference) preference).getEntries()[Integer.valueOf((String) value)]);
                } else if (preference instanceof SwitchPreference) {
                    ((SwitchPreference) preference).setDisableDependentsState((boolean) value);
                }
                return true;
            }
            return false;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference instanceof EditTextPreference || preference instanceof ListPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
        if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_call_smooth_mode))) {
            ((SwitchPreference) preference).setDisableDependentsState(JCManager.getInstance().config.call.getVideoSmoothMode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || CallPreferenceFragment.class.getName().equals(fragmentName)
                || ConferencePreferenceFragment.class.getName().equals(fragmentName)
                || AboutPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_display_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_server)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_appkey)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CallPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_call);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_call_max_num)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_call_audio_conference)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_call_smooth_mode)));
            findPreference(getString(R.string.cloud_setting_key_c_codec)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(preference.getContext(), CCodecActivity.class);
                    preference.getContext().startActivity(intent);
                    return false;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ConferencePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_conference);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_max_num)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_cdn_address)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_qiniu_bucket_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_qiniu_secret_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_qiniu_access_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_qiniu_file_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_request_video_size)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_max_resolution)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.cloud_setting_key_conference_key_interval)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private static boolean setToSdkIfNeed(Preference preference, Object value) {
        if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_display_name))) {
            JCManager.getInstance().client.setDisplayName((String) value);
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_server))) {
            JCManager.getInstance().client.setConfig(JCClient.CONFIG_KEY_SERVER_ADDRESS, (String) value);
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_call_max_num))) {
            if (TextUtils.isEmpty((String) value)) {
                return false;
            }
            int num = Integer.parseInt((String) value);
            if (num >= 1) {
                JCManager.getInstance().call.maxCallNum = num;
            } else {
                return false;
            }
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_call_audio_conference))) {
            JCManager.getInstance().call.setConference((boolean) value);
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_call_smooth_mode))) {
            JCManager.getInstance().config.call.setVideoSmoothMode((boolean) value);
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_max_num))) {
            if (TextUtils.isEmpty((String) value)) {
                return false;
            }
            int num = Integer.parseInt((String) value);
            if (num >= 2 && num <= 16) {
                JCManager.getInstance().mediaChannel.setConfig(JCMediaChannel.CONFIG_CAPACITY, String.valueOf(num));
            } else {
                return false;
            }
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_cdn_address))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_qiniu_bucket_name))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_qiniu_secret_key))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_qiniu_access_key))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_qiniu_file_name))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_request_video_size))) {
            return true;
        } else if (TextUtils.equals(preference.getKey(), preference.getContext().getString(R.string.cloud_setting_key_conference_max_resolution))) {
            int width = 640;
            int height = 360;
            switch (Integer.valueOf((String) value)) {
                case 1:
                    width = 1280;
                    height = 720;
                    break;
                case 2:
                    width = 1920;
                    height = 1080;
                    break;
            }
            JCManager.getInstance().mediaDevice.setCameraProperty(width, height, 30);
            return true;
        }
        return true;
    }

    public static final class AboutPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            setHasOptionsMenu(true);
            final Context context = getActivity();
            String version = String.format(context.getString(R.string.pref_about_version_template),
                    Utils.getAppVersionName(context), Utils.getAppVersionCode(context));
            findPreference(getString(R.string.cloud_setting_key_about_app_version)).setSummary(version);
            findPreference(getString(R.string.cloud_setting_key_about_avatar_version)).setSummary(MtcVer.Mtc_GetAvatarVersion());
            findPreference(getString(R.string.cloud_setting_key_about_giraffe_version)).setSummary(MtcVer.Mtc_GetGiraffeVersion());
            findPreference(getString(R.string.cloud_setting_key_about_grape_version)).setSummary(Zmf.getVersion());
            findPreference(getString(R.string.cloud_setting_key_about_jsm_version)).setSummary(MtcConf.Mtc_GetJsmVersion());
            findPreference(getString(R.string.cloud_setting_key_about_lemon_version)).setSummary(MtcVer.Mtc_GetLemonVersion());
            findPreference(getString(R.string.cloud_setting_key_about_melon_version)).setSummary(MtcMedia.Mtc_GetMelonVersion());
            findPreference(getString(R.string.cloud_setting_key_about_watermelon_version)).setSummary(MtcMedia.Mtc_GetMmeVersion());

            findPreference(getString(R.string.cloud_setting_key_about_upload_log)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final EditText et = new EditText(context);
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.prompt_reason)
                            .setView(et)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String reason = et.getText().toString().trim();
                                    if (TextUtils.isEmpty(reason)) {
                                        Toast.makeText(context, "please enter reason", Toast.LENGTH_SHORT).show();
                                    } else {
                                        JCLog.uploadLog(reason);
                                    }
                                }
                            }).setNegativeButton(android.R.string.cancel,null).show();
                    return false;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
