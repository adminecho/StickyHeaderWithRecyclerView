package com.echoit.stickyheader;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by EchoIT on 6/20/2018.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{

    private List<ContactVO> contactVOList = null;
    private Context mContext;

    public AllContactsAdapter(Context mContext){
        this.contactVOList = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        holder.tvEmail.setText(contactVO.getContactEmail());

        holder.ivContactImage.setImageResource(R.drawable.user_profile_round);
       if (contactVOList.get(position).getContactImage() != null && !contactVOList.get(position).getContactImage().isEmpty()) {

           holder.ivContactImage.setImageURI(Uri.parse(contactVOList.get(position).getContactImage()));
            Glide.with(mContext).load(contactVOList.get(position).getContactImage()).asBitmap().placeholder(R.drawable.user_profile_round).error(R.drawable.user_profile_round).into(new BitmapImageViewTarget(holder.ivContactImage) {
               @Override
                protected void setResource(Bitmap resource) {
                   RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                   circularBitmapDrawable.setCircular(true);
                  holder.ivContactImage.setImageDrawable(circularBitmapDrawable);
               }
          });
       } else {
           holder.ivContactImage.setImageResource(R.drawable.user_profile_round);
       }
    }

    public  void addData(List<ContactVO> mContactVOList)
    {
        contactVOList = new ArrayList<>();
        if(mContactVOList!=null)
        contactVOList.addAll(mContactVOList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView tvEmail;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
        }
    }
}
