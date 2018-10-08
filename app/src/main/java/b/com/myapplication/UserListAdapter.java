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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private String userName;
    private ArrayList<RegisterModal> notificationList;
    private HomeActivity context;

    UserListAdapter(String userName, ArrayList<RegisterModal> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = (HomeActivity) context;
        this.userName = userName;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_layout, parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListAdapter.ViewHolder holder, final int position) {
        RegisterModal registerModal = notificationList.get(position);

        holder.usename.setText(registerModal.name);
        if (registerModal.name.equals(userName)) {
            holder.admin.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView usename, admin;
        LinearLayout mainView;

        ViewHolder(View itemView) {
            super(itemView);
            usename = itemView.findViewById(R.id.usename);
            mainView = itemView.findViewById(R.id.mainView);
            admin = itemView.findViewById(R.id.admin);
            mainView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RegisterModal registerModall = notificationList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.mainView:
                    if (registerModall.name.equals(userName)) {
                        Toast.makeText(context, "You are admin", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, registerModall.email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("id", registerModall);
                        intent.putExtra("tocan", registerModall.deviceToken);
                        intent.putExtra("userName", userName);
                        context.startActivity(intent);
                    }

                    break;
            }
        }
    }
}
