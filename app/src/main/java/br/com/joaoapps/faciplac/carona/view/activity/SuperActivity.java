package br.com.joaoapps.faciplac.carona.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.dialogs.LoadDialogFragment;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.MessageActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by joao.roriz on 03/04/18.
 */

public class SuperActivity extends AppCompatActivity {
    private static LoadDialogFragment loadDialogFragment;

    protected void setupToolbar(String title) {
        setupToolbar(true, title);
    }

    protected void setupToolbar(boolean visibleBack, String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
            actionBar.setElevation(0);
        } else {
            Toolbar toolbar = findById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
                if (toolbarTitle != null) {
                    toolbarTitle.setText(title);
                    if (!toolbarTitle.getText().toString().isEmpty()) {
                        toolbarTitle.setVisibility(View.VISIBLE);
                        toolbarTitle.setText(title);
                    }
                }
                View backButton = toolbar.findViewById(R.id.toolbar_back);
                if (backButton != null) {
                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SuperActivity.this.onBackPressed();
                        }
                    });
                }

                if (!visibleBack) {
                    assert backButton != null;
                    backButton.setVisibility(View.GONE);
                } else {
                    assert backButton != null;
                    backButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    protected void startActivityAnimBottomToUp(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_up_out);
    }

    protected void finishActivityAnimTopDown(Intent intent) {
        finish();
        overridePendingTransition(R.anim.activity_bottom_out, R.anim.activity_up_out);

    }

    protected void finishAnim() {
        overridePendingTransition(R.anim.activity_out_back, R.anim.activity_in_back);
    }

    public <T extends View> T findById(int id) {
        return (T) this.findViewById(id);
    }

    public String getTextString(int id) {
        EditText t = (EditText) findViewById(id);
        String s = "";
        if (t != null) {
            s = t.getText().toString();
        }
        return s;
    }

    public String getTextString(EditText editText) {
        String s = "";
        if (editText != null) {
            s = editText.getText().toString();
        }
        return s;
    }


    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getWindow().getDecorView();
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    protected void replaceFragment(final int layoutConteudo, final Fragment fragment, final boolean permiteVoltar, final boolean showAnim, final boolean next) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().executePendingTransactions();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (showAnim) {
                    if (next) {
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    } else {
                        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                    }

                }
                if (permiteVoltar) {
                    transaction.addToBackStack(fragment.getClass().getName());
                }
                transaction.replace(layoutConteudo, fragment, fragment.getClass().getName());
                transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.commit();
            }
        });
    }


    protected void replaceFragment(int layoutConteudo, Fragment fragment, boolean permiteVoltar) {
        replaceFragment(layoutConteudo, fragment, permiteVoltar, false, false);
    }

    protected void replaceFragment(int layoutConteudo, Fragment fragmentAnterior, Fragment fragment) {
        getSupportFragmentManager().executePendingTransactions();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(fragmentAnterior.getClass().getName());
        transaction.hide(fragmentAnterior);
        transaction.add(layoutConteudo, fragment);
        transaction.commit();
    }

    protected Fragment getCurrentFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (getFragmentManager().getBackStackEntryCount() == 0)
            return null;
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentByTag(fragmentTag);
        return (Fragment) currentFragment;
    }

    public void startActivityClearingOthers(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static void startActivityMessage(Context context, Intent intent, String title, String message) {
        Intent intentMessage = new Intent(context, MessageActivity.class);
        intentMessage.putExtra(MessageActivity.TITLE, title);
        intentMessage.putExtra(MessageActivity.SUBTITLE, message);
        intentMessage.putExtra(MessageActivity.INTENT, intent);
        context.startActivity(intentMessage);
    }

    public static void startActivityMessagePositive(Context context, Intent intent, String message) {
        Intent intentMessage = new Intent(context, MessageActivity.class);
        intentMessage.putExtra(MessageActivity.SUBTITLE, message);
        intentMessage.putExtra(MessageActivity.INTENT, intent);
        intentMessage.putExtra(MessageActivity.CODE_ICON, MessageActivity.ICON_SUCCESS);
        context.startActivity(intentMessage);
    }

    public static void startActivityMessageNegative(Context context, Intent intent, String message) {
        Intent intentMessage = new Intent(context, MessageActivity.class);
        intentMessage.putExtra(MessageActivity.SUBTITLE, message);
        intentMessage.putExtra(MessageActivity.INTENT, intent);
        intentMessage.putExtra(MessageActivity.CODE_ICON, MessageActivity.ICON_ERROR);
        context.startActivity(intentMessage);
    }

    public static void showDialogLoad(AppCompatActivity context) {
        if (loadDialogFragment != null && loadDialogFragment.getDialog().isShowing()) {
            loadDialogFragment.dismiss();
        }
        FragmentManager fm = context.getSupportFragmentManager();
        loadDialogFragment = new LoadDialogFragment().newInstance();
        loadDialogFragment.show(fm, "dialog");
    }


    public static void closeDialogLoad() {
        if (loadDialogFragment != null && loadDialogFragment.getDialog() != null) {
            if (loadDialogFragment.getDialog().isShowing())
                loadDialogFragment.dismiss();
        }
        loadDialogFragment = null;
    }


}
