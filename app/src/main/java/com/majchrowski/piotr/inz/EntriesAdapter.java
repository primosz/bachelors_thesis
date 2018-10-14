package com.majchrowski.piotr.inz;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntriesViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mlistener;

    public interface  OnItemClickListener{
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public EntriesAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }



    public class EntriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView idText, typeText, nameText, categoryText, dateText, valueText;

        public EntriesViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            idText = itemView.findViewById(R.id.tvItemId);
            nameText = itemView.findViewById(R.id.tvItemName);
            typeText = itemView.findViewById(R.id.tvItemType);
            categoryText = itemView.findViewById(R.id.tvItemCategory);
            dateText = itemView.findViewById(R.id.tvItemDate);
            valueText = itemView.findViewById(R.id.tvItemValue);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlistener!=null){
                        int position =getAdapterPosition();
                        v=itemView;
                        if (position!= RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position, v);
                        }
                    }
                }
            });


        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public EntriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.entry_item, parent, false);
        return new EntriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntriesViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        int id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.NAME));
        int type = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.TYPE_ID));
        int category = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.CATEGORY_ID));
        String date = mCursor.getString((mCursor.getColumnIndex(DatabaseHelper.DATE)));
        double value = mCursor.getDouble(mCursor.getColumnIndex(DatabaseHelper.VALUE));
        holder.idText.setText(String.valueOf(id));
        holder.typeText.setText(String.valueOf(type));
        holder.categoryText.setText(String.valueOf(category));
        holder.valueText.setText(String.valueOf(value));
        holder.dateText.setText(date);
        holder.nameText.setText(name);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor!=null) mCursor.close();
        mCursor = newCursor;
        if(newCursor!=null) notifyDataSetChanged();
    }


}
