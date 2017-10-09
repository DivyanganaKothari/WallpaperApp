package trainedge.social_wallpaper_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;


public class InfoAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<SocialModel> List;

    public InfoAdapter(List<SocialModel> infolist) {
        List = infolist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_holder, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SocialModel infolist = List.get(position);
        Glide.with(holder.iv.getContext()).load(infolist.url).into(holder.iv);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.iv.getContext(),infolist.url,Toast.LENGTH_SHORT).show();
            }
        });
        }

    @Override
    public int getItemCount()
    {
        return List.size() ;
    }
}


