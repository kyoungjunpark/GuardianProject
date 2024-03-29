package com.example.administrator.guardian.ui.adapter;

/**
 * Created by Administrator on 2016-05-06.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.guardian.R;
import com.example.administrator.guardian.datamodel.VolunteerTimeRecyclerItem;
import com.example.administrator.guardian.ui.activity.Volunteer.VolunteerFragmentThreeAwardActivity;

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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_time_cardview, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VolunteerTimeRecyclerItem item=items.get(position);

        holder.volunteerdate.setText(item.getYear()+"."+item.getMonth()+"."+item.getDay()+" "+item.getHour()+":"+item.getMinute()+" "+item.getReq_hour()+"시간");
        holder.volunteersenior.setText(item.getSenior_name()+" 님을 방문하여 봉사함을 인정함.");

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VolunteerFragmentThreeAwardActivity.class);
                // senior_id, senior_name, startInfo, startInfo(year,month, day, hour, minute), req_hour
                intent.putExtra("id",item.getVol_name());
                intent.putExtra("senior_id",item.getSenior_id());
                intent.putExtra("startInfo", item.getStartInfo());
                intent.putExtra("year",item.getYear());
                intent.putExtra("month",item.getMonth());
                intent.putExtra("day",item.getDay());
                intent.putExtra("hour",item.getHour());
                intent.putExtra("minute",item.getMinute());
                intent.putExtra("req_hour",item.getReq_hour());
                intent.putExtra("name",item.getSenior_name());
                context.startActivity(intent);
            }
        });
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

            cardview=(CardView)itemView.findViewById(R.id.volunteer_time_cardview);
        }
    }
}
