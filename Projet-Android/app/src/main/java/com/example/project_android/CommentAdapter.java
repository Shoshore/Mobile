package com.example.project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentModel> comments;

    public CommentAdapter(List<CommentModel> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel c = comments.get(position);
        holder.tvAuthor.setText("👤 " + c.getAuthor());
        holder.tvText.setText(c.getText());
        holder.tvDate.setText(c.getDate());
    }

    @Override
    public int getItemCount() { return comments.size(); }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvText, tvDate;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_comment_author);
            tvText   = itemView.findViewById(R.id.tv_comment_text);
            tvDate   = itemView.findViewById(R.id.tv_comment_date);
        }
    }
}