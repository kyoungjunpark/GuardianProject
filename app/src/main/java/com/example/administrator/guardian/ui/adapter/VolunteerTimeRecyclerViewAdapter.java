package com.example.administrator.guardian.ui.adapter;

/**
 * Created by Administrator on 2016-05-06.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.guardian.R;
import com.example.administrator.guardian.datamodel.VolunteerTimeRecyclerItem;

import java.util.List;

/**
 * Created by Administrator on 2016-04-28.
 */
public class VolunteerTimeRecyclerViewAdapter extends RecyclerView.Adapter<VolunteerTimeRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<VolunteerTimeRecyclerItem> items;
    int item_layout;

    public VolunteerTimeRecyclerViewAdapter(Context context, List<VolunteerTimeRecyclerItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_cardview, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VolunteerTimeRecyclerItem item=items.get(position);

        holder.volunteerdate.setText(item.getYear()+"."+item.getMonth()+"."+item.getDay()+"/"+item.getStartHour()+":"+item.getStartMinute()+"~"+item.getFinishHour()+":"+item.getFinishMinute());
        holder.volunteersenior.setText(item.getName()+" 님을 방문하여 봉사함을 인정함.");

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView volunteerdate;
        TextView volunteersenior;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            volunteerdate=(TextView)itemView.findViewById(R.id.volunteerdate);
            volunteersenior=(TextView)itemView.findViewById(R.id.volunteersenior);

            cardview=(CardView)itemView.findViewById(R.id.volunteer_cardview);
        }
    }
}