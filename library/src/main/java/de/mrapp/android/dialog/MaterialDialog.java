package de.mrapp.android.dialog;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Michael Rapp
 * @since 2.0.1
 */
public class MaterialDialog extends AlertDialog {

    private final AlertDialog wrappedDialog;

    public MaterialDialog(final AlertDialog dialog) {
        super(dialog.getContext());
        this.wrappedDialog = dialog;
    }

    @Override
    public final Button getButton(final int whichButton) {
        return wrappedDialog.getButton(whichButton);
    }

    @Override
    public final ListView getListView() {
        return wrappedDialog.getListView();
    }

    @Override
    public final void setTitle(final CharSequence title) {
        wrappedDialog.setTitle(title);
    }

    @Override
    public final void setCustomTitle(final View customTitleView) {
        wrappedDialog.setCustomTitle(customTitleView);
    }

    @Override
    public final void setMessage(final CharSequence message) {
        wrappedDialog.setMessage(message);
    }

    @Override
    public final void setView(final View view) {
        wrappedDialog.setView(view);
    }

    @Override
    public final void setView(final View view, final int viewSpacingLeft, final int viewSpacingTop,
                              final int viewSpacingRight, final int viewSpacingBottom) {
        wrappedDialog.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight,
                viewSpacingBottom);
    }

    @Override
    public final void setButton(final int whichButton, final CharSequence text, final Message msg) {
        wrappedDialog.setButton(whichButton, text, msg);
    }

    @Override
    public final void setButton(final int whichButton, final CharSequence text,
                                final OnClickListener listener) {
        wrappedDialog.setButton(whichButton, text, listener);
    }

    @Override
    public final void setButton(final CharSequence text, final Message msg) {
        wrappedDialog.setButton(text, msg);
    }

    @Override
    public final void setButton2(final CharSequence text, final Message msg) {
        wrappedDialog.setButton(text, msg);
    }

    @Override
    public final void setButton3(final CharSequence text, final Message msg) {
        wrappedDialog.setButton3(text, msg);
    }

    @Override
    public final void setButton(final CharSequence text, final OnClickListener listener) {
        wrappedDialog.setButton(text, listener);
    }

    @Override
    public final void setButton2(final CharSequence text, final OnClickListener listener) {
        wrappedDialog.setButton2(text, listener);
    }

    @Override
    public final void setButton3(final CharSequence text, final OnClickListener listener) {
        wrappedDialog.setButton3(text, listener);
    }

    @Override
    public final void setIcon(@DrawableRes final int resId) {
        wrappedDialog.setIcon(resId);
    }

    @Override
    public final void setIcon(final Drawable icon) {
        wrappedDialog.setIcon(icon);
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attrId) {
        wrappedDialog.setIconAttribute(attrId);
    }

    @Override
    public final void setInverseBackgroundForced(final boolean forceInverseBackground) {
        wrappedDialog.setInverseBackgroundForced(forceInverseBackground);
    }

    @Override
    public final boolean onKeyDown(final int keyCode, final KeyEvent event) {
        return wrappedDialog.onKeyDown(keyCode, event);
    }

    @Override
    public final boolean onKeyUp(int keyCode, KeyEvent event) {
        return wrappedDialog.onKeyUp(keyCode, event);
    }

    @Override
    public final ActionBar getActionBar() {
        return wrappedDialog.getActionBar();
    }

    @Override
    public final boolean isShowing() {
        return wrappedDialog.isShowing();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void create() {
        wrappedDialog.create();
    }

    @Override
    public final void show() {
        wrappedDialog.show();
    }

    @Override
    public void hide() {
        wrappedDialog.hide();
    }

    @Override
    public void dismiss() {
        wrappedDialog.dismiss();
    }

    @Override
    public final Bundle onSaveInstanceState() {
        return wrappedDialog.onSaveInstanceState();
    }

    @Override
    public final void onRestoreInstanceState(final Bundle savedInstanceState) {
        wrappedDialog.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public final Window getWindow() {
        return wrappedDialog.getWindow();
    }

    @Override
    public final View getCurrentFocus() {
        return wrappedDialog.getCurrentFocus();
    }

    @Override
    @Nullable
    public final View findViewById(@IdRes final int id) {
        return wrappedDialog.findViewById(id);
    }

    @Override
    public final void setContentView(@LayoutRes final int layoutResID) {
        wrappedDialog.setContentView(layoutResID);
    }

    @Override
    public final void setContentView(final View view) {
        wrappedDialog.setContentView(view);
    }

    @Override
    public final void setContentView(final View view, final ViewGroup.LayoutParams params) {
        wrappedDialog.setContentView(view, params);
    }

    @Override
    public final void addContentView(final View view, final ViewGroup.LayoutParams params) {
        wrappedDialog.addContentView(view, params);
    }

    @Override
    public void setTitle(@StringRes final int titleId) {
        wrappedDialog.setTitle(titleId);
    }

    @Override
    public final boolean onKeyLongPress(final int keyCode, final KeyEvent event) {
        return wrappedDialog.onKeyLongPress(keyCode, event);
    }

    @Override
    public final boolean onKeyMultiple(final int keyCode, final int repeatCount,
                                       final KeyEvent event) {
        return wrappedDialog.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public final void onBackPressed() {
        wrappedDialog.onBackPressed();
    }

    @Override
    public final boolean onKeyShortcut(final int keyCode, final KeyEvent event) {
        return wrappedDialog.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return wrappedDialog.onTouchEvent(event);
    }

    @Override
    public final boolean onTrackballEvent(final MotionEvent event) {
        return wrappedDialog.onTrackballEvent(event);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public final boolean onGenericMotionEvent(final MotionEvent event) {
        return wrappedDialog.onGenericMotionEvent(event);
    }

    @Override
    public final void onWindowAttributesChanged(final WindowManager.LayoutParams params) {
        wrappedDialog.onWindowAttributesChanged(params);
    }

    @Override
    public final void onContentChanged() {
        wrappedDialog.onContentChanged();
    }

    @Override
    public final void onWindowFocusChanged(final boolean hasFocus) {
        wrappedDialog.onWindowFocusChanged(hasFocus);
    }

    @Override
    public final void onAttachedToWindow() {
        wrappedDialog.onAttachedToWindow();
    }

    @Override
    public final void onDetachedFromWindow() {
        wrappedDialog.onDetachedFromWindow();
    }

    @Override
    public final boolean dispatchKeyEvent(final KeyEvent event) {
        return wrappedDialog.dispatchKeyEvent(event);
    }

    @Override
    public final boolean dispatchKeyShortcutEvent(final KeyEvent event) {
        return wrappedDialog.dispatchKeyShortcutEvent(event);
    }

    @Override
    public final boolean dispatchTouchEvent(final MotionEvent ev) {
        return wrappedDialog.dispatchTouchEvent(ev);
    }

    @Override
    public final boolean dispatchTrackballEvent(final MotionEvent ev) {
        return wrappedDialog.dispatchTrackballEvent(ev);
    }

    @Override
    public final boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent event) {
        return wrappedDialog.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public final boolean dispatchGenericMotionEvent(final MotionEvent ev) {
        return wrappedDialog.dispatchGenericMotionEvent(ev);
    }

    @Override
    public final View onCreatePanelView(final int featureId) {
        return wrappedDialog.onCreatePanelView(featureId);
    }

    @Override
    public final boolean onCreatePanelMenu(final int featureId, Menu menu) {
        return wrappedDialog.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public final boolean onPreparePanel(final int featureId, final View view, final Menu menu) {
        return wrappedDialog.onPreparePanel(featureId, view, menu);
    }

    @Override
    public final boolean onMenuOpened(final int featureId, final Menu menu) {
        return wrappedDialog.onMenuOpened(featureId, menu);
    }

    @Override
    public final boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        return wrappedDialog.onMenuItemSelected(featureId, item);
    }

    @Override
    public final void onPanelClosed(final int featureId, final Menu menu) {
        wrappedDialog.onPanelClosed(featureId, menu);
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        return wrappedDialog.onCreateOptionsMenu(menu);
    }

    @Override
    public final boolean onPrepareOptionsMenu(final Menu menu) {
        return wrappedDialog.onPrepareOptionsMenu(menu);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        return wrappedDialog.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        wrappedDialog.onOptionsMenuClosed(menu);
    }

    @Override
    public final void openOptionsMenu() {
        wrappedDialog.openOptionsMenu();
    }

    @Override
    public final void closeOptionsMenu() {
        wrappedDialog.closeOptionsMenu();
    }

    @Override
    public final void invalidateOptionsMenu() {
        wrappedDialog.invalidateOptionsMenu();
    }

    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v,
                                          final ContextMenu.ContextMenuInfo menuInfo) {
        wrappedDialog.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public final void registerForContextMenu(final View view) {
        wrappedDialog.registerForContextMenu(view);
    }

    @Override
    public final void unregisterForContextMenu(final View view) {
        wrappedDialog.unregisterForContextMenu(view);
    }

    @Override
    public final void openContextMenu(final View view) {
        wrappedDialog.openContextMenu(view);
    }

    @Override
    public final boolean onContextItemSelected(final MenuItem item) {
        return wrappedDialog.onContextItemSelected(item);
    }

    @Override
    public final void onContextMenuClosed(final Menu menu) {
        wrappedDialog.onContextMenuClosed(menu);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public final boolean onSearchRequested(final SearchEvent searchEvent) {
        return wrappedDialog.onSearchRequested(searchEvent);
    }

    @Override
    public final boolean onSearchRequested() {
        return wrappedDialog.onSearchRequested();
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return wrappedDialog.onWindowStartingActionMode(callback);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public final ActionMode onWindowStartingActionMode(final ActionMode.Callback callback,
                                                       final int type) {
        return wrappedDialog.onWindowStartingActionMode(callback, type);
    }

    @Override
    @CallSuper
    public final void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);
        wrappedDialog.onActionModeStarted(mode);
    }

    @Override
    @CallSuper
    public final void onActionModeFinished(final ActionMode mode) {
        super.onActionModeFinished(mode);
        wrappedDialog.onActionModeFinished(mode);
    }

    @Override
    public final void takeKeyEvents(final boolean get) {
        wrappedDialog.takeKeyEvents(get);
    }

    @Override
    public final LayoutInflater getLayoutInflater() {
        return wrappedDialog.getLayoutInflater();
    }

    @Override
    public final void setCancelable(final boolean flag) {
        wrappedDialog.setCancelable(flag);
    }

    @Override
    public final void setCanceledOnTouchOutside(final boolean cancel) {
        wrappedDialog.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public final void cancel() {
        wrappedDialog.cancel();
    }

    @Override
    public final void setOnCancelListener(final OnCancelListener listener) {
        wrappedDialog.setOnCancelListener(listener);
    }

    @Override
    public final void setCancelMessage(final Message msg) {
        wrappedDialog.setCancelMessage(msg);
    }

    @Override
    public final void setOnDismissListener(final OnDismissListener listener) {
        wrappedDialog.setOnDismissListener(listener);
    }

    @Override
    public final void setOnShowListener(final OnShowListener listener) {
        wrappedDialog.setOnShowListener(listener);
    }

    @Override
    public final void setDismissMessage(final Message msg) {
        wrappedDialog.setDismissMessage(msg);
    }

    @Override
    public final void setOnKeyListener(final OnKeyListener onKeyListener) {
        wrappedDialog.setOnKeyListener(onKeyListener);
    }

}