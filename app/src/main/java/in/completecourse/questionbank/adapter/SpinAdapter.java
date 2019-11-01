package in.completecourse.questionbank.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import in.completecourse.questionbank.R;

public class SpinAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mLayoutInflater;
    private final String[] mStringArray;
    private final int mResource;

    public SpinAdapter(@NonNull Context context, int resource, String[] strArr) {
        super(context, resource, 0, strArr);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.mStringArray = strArr;
    }

    private View homeView(int i, ViewGroup viewGroup){
        View view = this.mLayoutInflater.inflate(this.mResource, viewGroup, false);

        switch (i%3){
            case 0:
            case 2:
                ((TextView)view.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[i] +
                        "<sup><small>th</small></sup>"));
                ((TextView)view.findViewById(R.id.tvclass)).setText("Class");
                break;
            case 1:
                ((TextView)view.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[i] +
                        "<sup><small>वीं</small></sup>"));
                ((TextView)view.findViewById(R.id.tvclass)).setText("कक्षा");
                break;

        }

        return view;
    }

    @Override
    public View getDropDownView(int i, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null){
            view = this.mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false);
        }

        switch (i%3){
            case 0:
            case 2:
                ((TextView)view.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[i] +
                        "<sup><small>th</small></sup>"));
                ((TextView)view.findViewById(R.id.tvclass)).setText("Class");
                break;
            case 1:
                ((TextView)view.findViewById(R.id.offer_type_txt)).setText(Html.fromHtml(this.mStringArray[i] +
                        "<sup><small>वीं</small></sup>"));
                ((TextView)view.findViewById(R.id.tvclass)).setText("कक्षा");
                break;

        }

        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           return homeView(position, parent);
    }
}
