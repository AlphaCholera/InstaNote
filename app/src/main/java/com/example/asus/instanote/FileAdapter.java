package com.example.asus.instanote;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> Data;

    FileAdapter(ArrayList<String> FILES, Context context) {
        this.Data = FILES;
        this.context = context;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fileview;
        TextView contents;
        TextView textViewOptions;
        MyViewHolder(View itemView) {
            super(itemView);
            this.fileview = itemView.findViewById(R.id.fileview);
            this.contents = itemView.findViewById(R.id.contents);
            this.textViewOptions = itemView.findViewById(R.id.textViewOptions);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        // Get the name of the note
        String name_of_the_file = Data.get(position);
        // Set the name of the file in the holder
        holder.fileview.setText(name_of_the_file);
        // Set the contents of the file in the holder
        holder.contents.setText(FileHandling.contentsOfFile(name_of_the_file, context));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File file_to_open = new File(context.getFilesDir(), "MyDir");
//                String name = file_to_open.getName();
//                Toast.makeText(context, "Name of the file is "+name, Toast.LENGTH_SHORT).show();

//                openFile(holder,v);
//            }
//        });
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.textViewOptions);
                popupMenu.inflate(R.menu.recycler_view_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {



                            case R.id.delete :
                                new AlertDialog.Builder(v.getContext()).setTitle("Are you sure you want to delete this note?")
                                    .setPositiveButton("Yeah, pretty sure..!!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (FileHandling.deleteFile(holder.fileview.getText().toString(),context)) {
                                            Toast.makeText(context, "Deleted Successfully..!!", Toast.LENGTH_SHORT).show();
                                            Data.remove(holder.getAdapterPosition());
                                            notifyItemRemoved(holder.getAdapterPosition());
                                        }
                                        else
                                            Toast.makeText(context, "Error in deletion..!!", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("Nope..!!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                                break;



                            case R.id.open :
                                openFile(holder,v);
                                break;



                            case R.id.rename :
                                final String old_file_name = holder.fileview.getText().toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()).setTitle("Rename Your Note");

//                                LayoutInflater factory = LayoutInflater.from(context);
//                                final View textEntryView = factory.inflate(R.layout.rename_file_layout, null);
//                                builder.setView(textEntryView);
//                                final EditText new_file_name_edit_text = textEntryView.findViewById(R.id.renamefile);

                                final EditText editText = new EditText(context);
                                editText.setHint("Enter the new name of your note here");
                                builder.setIcon(R.drawable.appicon);
                                builder.setView(editText);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String new_file_name = editText.getText().toString();
                                        if (FileHandling.renameFile(old_file_name,new_file_name,context)) {
                                            Toast.makeText(context, "Renamed Note Successfully", Toast.LENGTH_SHORT).show();
                                            // Notify The Recycler View
                                        }
                                        else
                                            Toast.makeText(context, "Error in renaming file..!!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                                break;



                            case R.id.share :
                                FileHandling.shareFile(v, context, FileHandling.contentsOfFile(holder.fileview.getText().toString(), context));
                                break;
                        }
                        return false;
                    }
                });

            }
        });
    }

    private void openFile(MyViewHolder holder, View v) {
        SharedPreferences sp = context.getSharedPreferences("MySharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("FileName",holder.fileview.getText().toString());
        ed.putString("Contents",FileHandling.contentsOfFile(holder.fileview.getText().toString(),context));
        ed.putBoolean("isNewFile", true);
        ed.apply();
        Intent intent = new Intent(v.getContext(), Main2Activity.class);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
