package company.photoshooto_task.ViewHolder;

import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import company.photoshooto_task.Interface.OfferItemClickListener;
import company.photoshooto_task.R;
import company.photoshooto_task.RoundRectCornerImageView;

public class DailyPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RoundRectCornerImageView offerImage;
    public ImageView imgShare,imgZoom;
    public TextView shareCount;
    private OfferItemClickListener offerItemClickListener;

    public DailyPhotoViewHolder(View itemView) {
        super(itemView);
        offerImage=(RoundRectCornerImageView) itemView.findViewById(R.id.offer_image);
        imgShare=(ImageView) itemView.findViewById(R.id.img_share);
        imgZoom=(ImageView) itemView.findViewById(R.id.img_zoom);
        shareCount=(TextView) itemView.findViewById(R.id.share_count);

        itemView.setOnClickListener(this);
    }
    public void setOfferItemClickListener(OfferItemClickListener offerItemClickListener){
        this.offerItemClickListener=offerItemClickListener;
    }

    @Override
    public void onClick(View v) {

        offerItemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
