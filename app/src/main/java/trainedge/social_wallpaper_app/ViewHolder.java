package trainedge.social_wallpaper_app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by hp on 23-04-2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder {


      RelativeLayout rlcontainer;
      ImageView iv;

    public ViewHolder(View itemView) {
        super(itemView);
        rlcontainer = (RelativeLayout) itemView.findViewById(R.id.rlcontainer);
        iv = (ImageView) itemView.findViewById(R.id.iv);

    }
}
