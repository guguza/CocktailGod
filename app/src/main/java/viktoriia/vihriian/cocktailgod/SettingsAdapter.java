package viktoriia.vihriian.cocktailgod;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Администратор on 02.04.2015.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SViewHolder> {

    Filter filter;

    public SettingsAdapter(){
        filter = Filter.getInstance();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SettingsAdapter.SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent, false);
        SViewHolder holder = new SViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SettingsAdapter.SViewHolder holder, int pos) {
        holder.name.setText(filter.titleArray[pos]);
        holder.value.setText(filter.currentFilters[pos]);
    }

    @Override
    public int getItemCount() {
        return filter.titleArray.length;
    }

    public class SViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView value;
        CardView cv;

        public SViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            value = (TextView) v.findViewById(R.id.value);
            cv = (CardView) v.findViewById(R.id.cv);
        }
    }
}
