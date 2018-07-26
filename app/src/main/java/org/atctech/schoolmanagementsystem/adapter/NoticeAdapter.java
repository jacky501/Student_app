package org.atctech.schoolmanagementsystem.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.atctech.schoolmanagementsystem.R;
import org.atctech.schoolmanagementsystem.fragments.ViewBookFragment;
import org.atctech.schoolmanagementsystem.model.AllNotice;
import org.atctech.schoolmanagementsystem.utils.ConstantValue;

import java.util.List;

/**
 * Created by Jacky on 4/1/2018.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{


    private Context context;
    private List<AllNotice> allNotices;


    public NoticeAdapter(Context context, List<AllNotice> allNotices) {
        this.context = context;
        this.allNotices = allNotices;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_for_notice,null,false);

        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder,final int position) {

        holder.nsubjectName.setText(allNotices.get(position).getSubject());
        holder.ndate.setText(allNotices.get(position).getDate());

        int size = allNotices.get(position).getSize();
        int data = 1024;
        int KB = size/data;
        int MB = KB/data;
        holder.nsize.setText(MB+" MB");

        holder.ndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DownloadTask downloadTask = new DownloadTask(context);
//                downloadTask.execute("http://www.itp.uzh.ch/~suzanne/ebooks/The%20Web%20Book-A4-HM.pdf");

                String url = ConstantValue.PDF_URL+allNotices.get(position).getFile();

                String servicestring = Context.DOWNLOAD_SERVICE;
                DownloadManager downloadmanager;
                downloadmanager  = (DownloadManager)context.getSystemService(servicestring);
                Uri uri = Uri
                        .parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                long reference = downloadmanager.enqueue(request);
                Toast.makeText(context.getApplicationContext(),
                        "Your file is now downloading...", Toast.LENGTH_LONG).show();

            }
        });

        holder.nview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewBookFragment viewBookFragment = new ViewBookFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FILE_NAME",allNotices.get(position).getFile());
                viewBookFragment.setArguments(bundle);
                Activity activity = (Activity) view.getContext();
                activity.getFragmentManager().beginTransaction().replace(R.id.main_fragment,viewBookFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return allNotices != null ? allNotices.size() : 0;
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nsubjectName,ndate,nsize;
        private Button nview,ndownload;

        public NoticeViewHolder(View itemView) {
            super(itemView);

            nsubjectName = itemView.findViewById(R.id.noticeSubjectName);
            ndate = itemView.findViewById(R.id.noticeUploadDate);
            nsize = itemView.findViewById(R.id.noticeFileSize);
            nview = itemView.findViewById(R.id.noticeViewBtn);
            ndownload = itemView.findViewById(R.id.noticeDownloadBtn);
        }
    }
}
