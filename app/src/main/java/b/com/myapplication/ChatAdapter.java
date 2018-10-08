package b.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<ChetModal> chatList;
    private String userName;


    public ChatAdapter(String userName, ArrayList<ChetModal> chatList) {
        this.chatList = chatList;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatrtext_view, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, final int position) {
        ChetModal chetModal = chatList.get(position);
        holder.ownUser.setText(chetModal.msg);

        String checkUser  = chetModal.usename;

        if(!checkUser.equals(userName)){
            holder.otherUser.setVisibility(View.VISIBLE);
            holder.otherUser.setText(chetModal.msg);

        }else{
            holder.otherUser.setVisibility(View.GONE);
        }

        if(checkUser.equals(userName)){
            holder.ownUser.setVisibility(View.VISIBLE);
            holder.ownUser.setText(chetModal.msg);

        }else {
            holder.ownUser.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView ownUser,otherUser;
        ViewHolder(View itemView) {
            super(itemView);
            ownUser = itemView.findViewById(R.id.ownUser);
            otherUser = itemView.findViewById(R.id.otherUser);

        }
    }
}
