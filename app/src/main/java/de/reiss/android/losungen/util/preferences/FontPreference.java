package de.reiss.android.losungen.util.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.reiss.android.losungen.App;
import de.reiss.android.losungen.R;

public class FontPreference extends SpinnerPreference {

    private final LayoutInflater mLayoutInflater;

    @SuppressWarnings("unused")
    public FontPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected View createDropDownView(int position, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
    }

    @Override
    protected void bindDropDownView(int position, View view) {
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setTypeface(App.component.getAppPreferences().getTypefaces().get(mEntryValues[position]));
        textView.setText(mEntries[position]);
    }

}
