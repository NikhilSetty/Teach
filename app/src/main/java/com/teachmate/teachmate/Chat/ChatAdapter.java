package com.teachmate.teachmate.Chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.teachmate.teachmate.R;

import java.util.ArrayList;
public class ChatAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Message> mMessages;



	public ChatAdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	@Override
	public Object getItem(int position) {
		return mMessages.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_row, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.message_text);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.message.setText(message.getMessage());

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		if(message.isMine())
		{
			holder.message.setBackgroundResource(R.drawable.right);
			lp.gravity = Gravity.RIGHT;
		}
		else
		{
			holder.message.setBackgroundResource(R.drawable.left);
			lp.gravity = Gravity.LEFT;
		}
		holder.message.setLayoutParams(lp);
		return convertView;
	}
	private static class ViewHolder
	{
		TextView message;
	}
	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}
}
