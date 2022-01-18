package restrictkeyboard;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Detect weather device is rooted or not.
 */
public class RestrictKeyboard extends CordovaPlugin {

    private final String ERROR_UNKNOWN_ACTION = "Unknown action";

    public static final String LOG_TAG = "RestrictKeyboard";

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action == null) {
            cordova.getActivity().runOnUiThread(() -> {
                LOG.e(LOG_TAG, ERROR_UNKNOWN_ACTION);

                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, ERROR_UNKNOWN_ACTION));
            });

            return false;
        }

        switch (action) {
            case "isDefaultKeyboard":
                cordova.getThreadPool().execute(new Runnable() {

                    @Override
                    public void run() {
                        PluginResult result;
                        try {
                            result = new PluginResult(PluginResult.Status.OK, isUsingDefaultKeyboard());
                            LOG.e(LOG_TAG, "Default Keyboard:" + isUsingDefaultKeyboard());
                        } catch (Exception error) {
                            result =  new PluginResult(PluginResult.Status.ERROR, error.toString());
                        }

                        callbackContext.sendPluginResult(result);
                    }
                });

                return true;
            default:
                cordova.getActivity().runOnUiThread(new Runnable() {

                    public void run() {
                        LOG.e(LOG_TAG, ERROR_UNKNOWN_ACTION);
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, ERROR_UNKNOWN_ACTION));
                    }
                });

                return false;
        }
    }

    private boolean isUsingDefaultKeyboard() {
        Context context = this.cordova.getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> inputMethodList =
                inputMethodManager.getInputMethodList();
        for (InputMethodInfo inputMethodInfo : inputMethodList)
            if
            (inputMethodInfo.getId().equals(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD)))
                if ((inputMethodInfo.getServiceInfo().applicationInfo.flags &
                        ApplicationInfo.FLAG_SYSTEM) == 0)
                    return false;
        return true;
    }

}
