package com.example.lopic.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lopic.Interface.OnActivityResult;
import com.example.lopic.Interface.OnRequestPermissionsResult;
import com.example.lopic.Model.InputModel;
import com.example.lopic.Model.JsonData;
import com.example.lopic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnActivityResult, OnRequestPermissionsResult {
    //For Camera
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static final int LAYOUT_PHOTO = 0;
    private static final int LAYOUT_SINGLE_ITEM_CHOICE = 1;
    private static final int LAYOUT_COMMENT = 2;

    public int layout_photo = R.layout.layout_photo_item;
    public int layout_choice = R.layout.layout_single_choice_item;
    public int layout_comment = R.layout.layout_comment_item;

    int currentHolderPosition;

    JsonData jsonData;
    Context context;
    Activity activity;

    public ItemsAdapter(JsonData data, Context ctx) {
        this.context = ctx;
        this.jsonData = data;
        this.activity = (Activity) ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;

        switch (viewType) {
            case LAYOUT_PHOTO:
                view = LayoutInflater.from(parent.getContext()).inflate(layout_photo, parent, false);
                return new ViewHolderPhoto(view);
            case LAYOUT_SINGLE_ITEM_CHOICE:
                view = LayoutInflater.from(parent.getContext()).inflate(layout_choice, parent, false);
                return new ViewHolderChoice(view);
            case LAYOUT_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(layout_comment, parent, false);
                return new ViewHolderComment(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder.getItemViewType() == LAYOUT_PHOTO) {
            ViewHolderPhoto viewHolderPhoto = (ViewHolderPhoto) holder;
            viewHolderPhoto.heading.setText(jsonData.getData().get(holder.getAdapterPosition()).getTitle());
            if (jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getBitmap() != null) {
                viewHolderPhoto.selectImg.setImageBitmap(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getBitmap());
                viewHolderPhoto.cancelIcon.setVisibility(View.VISIBLE);
                viewHolderPhoto.selectImg.setOnClickListener(view -> {
                    showEnlargedImage(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getBitmap());
                });
                viewHolderPhoto.cancelIcon.setOnClickListener(view -> {
                    viewHolderPhoto.selectImg.setImageDrawable(context.getResources().getDrawable(R.drawable.addimage));
                    viewHolderPhoto.selectImg.setEnabled(true);
                    jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setBitmap(null);
                    notifyItemChanged(viewHolderPhoto.getAdapterPosition());
                });
            } else {
                viewHolderPhoto.cancelIcon.setVisibility(View.GONE);
                viewHolderPhoto.selectImg.setEnabled(true);
                viewHolderPhoto.selectImg.setImageDrawable(context.getResources().getDrawable(R.drawable.addimage));
                viewHolderPhoto.selectImg.setOnClickListener(v -> {
                    currentHolderPosition = viewHolderPhoto.getAdapterPosition();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });
            }

        } else if (holder.getItemViewType() == LAYOUT_SINGLE_ITEM_CHOICE) {
            ViewHolderChoice viewHolderChoice = (ViewHolderChoice) holder;
            viewHolderChoice.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                // find which radio button is selected
                if (checkedId == R.id.option0) {
                    jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setOptionSelection(viewHolderChoice.optionA.getText().toString());
                } else if (checkedId == R.id.option1) {
                    jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setOptionSelection(viewHolderChoice.optionB.getText().toString());
                } else if (checkedId == R.id.option2) {
                    jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setOptionSelection(viewHolderChoice.optionC.getText().toString());
                }

            });
            viewHolderChoice.radioGroupHeading.setText(jsonData.getData().get(holder.getAdapterPosition()).getTitle());
            if (jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().size() == 3) {
                viewHolderChoice.optionA.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(0));
                viewHolderChoice.optionB.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(1));
                viewHolderChoice.optionC.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(2));
                viewHolderChoice.optionA.setVisibility(View.VISIBLE);
                viewHolderChoice.optionB.setVisibility(View.VISIBLE);
                viewHolderChoice.optionC.setVisibility(View.VISIBLE);
            } else if (jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().size() == 2) {
                viewHolderChoice.optionA.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(0));
                viewHolderChoice.optionB.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(1));
                viewHolderChoice.optionA.setVisibility(View.VISIBLE);
                viewHolderChoice.optionB.setVisibility(View.VISIBLE);
                viewHolderChoice.optionC.setVisibility(View.GONE);
            } else if (jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().size() == 1) {
                viewHolderChoice.optionA.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getOptions().get(0));
                viewHolderChoice.optionA.setVisibility(View.VISIBLE);
                viewHolderChoice.optionB.setVisibility(View.GONE);
                viewHolderChoice.optionC.setVisibility(View.GONE);
            }

        } else if (holder.getItemViewType() == LAYOUT_COMMENT) {
            ViewHolderComment viewHolderComment = (ViewHolderComment) holder;

            viewHolderComment.commentHeading.setText(jsonData.getData().get(holder.getAdapterPosition()).getTitle());
            if (jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getComment() != null) {
                viewHolderComment.etAddComment.setText(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getComment());
                viewHolderComment.etAddComment.setSelection(jsonData.getData().get(holder.getAdapterPosition()).getDataMap().getComment().length());
            } else
                viewHolderComment.etAddComment.setText(null);
            viewHolderComment.etAddComment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setComment(s.toString());
                    } else
                        jsonData.getData().get(holder.getAdapterPosition()).getDataMap().setComment(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolderComment.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        viewHolderComment.etAddComment.setVisibility(View.VISIBLE);
                    } else viewHolderComment.etAddComment.setVisibility(View.GONE);

                }
            });
        }
    }

    private void showEnlargedImage(Bitmap bitmap) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(R.layout.layout_show_image);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imageView = builder.findViewById(R.id.fullImage);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        imageView.setAdjustViewBounds(true);
        ConstraintLayout rootView = builder.findViewById(R.id.rootView);
        rootView.setOnClickListener(v -> builder.dismiss());
        builder.show();
    }


    @Override
    public int getItemViewType(int position) {
        switch (jsonData.getData().get(position).getType()) {
            case "PHOTO":
                return LAYOUT_PHOTO;
            case "SINGLE_CHOICE":
                return LAYOUT_SINGLE_ITEM_CHOICE;
            case "COMMENT":
                return LAYOUT_COMMENT;
            default:
                return LAYOUT_PHOTO;
        }

    }

    @Override
    public int getItemCount() {
        return jsonData.getData().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            jsonData.getData().get(currentHolderPosition).getDataMap().setBitmap(photo);
            notifyItemChanged(currentHolderPosition);
        }
    }


    @Override
    public void OnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    public class ViewHolderPhoto extends RecyclerView.ViewHolder {
        @BindView(R.id.imageHeading)
        TextView heading;
        @BindView(R.id.iv_addImage)
        ImageView selectImg;
        @BindView(R.id.cancel)
        ImageView cancelIcon;

        public ViewHolderPhoto(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolderChoice extends RecyclerView.ViewHolder {
        @BindView(R.id.radioHeading)
        TextView radioGroupHeading;
        @BindView(R.id.radioGroup)
        RadioGroup radioGroup;
        @BindView(R.id.option0)
        RadioButton optionA;
        @BindView(R.id.option1)
        RadioButton optionB;
        @BindView(R.id.option2)
        RadioButton optionC;

        public ViewHolderChoice(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder {
        @BindView(R.id.commentHeading)
        TextView commentHeading;
        @BindView(R.id.switchAddComment)
        Switch aSwitch;
        @BindView(R.id.et_addComment)
        EditText etAddComment;

        public ViewHolderComment(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void printDataToLogcat() {
        for (InputModel row : jsonData.getData()) {
            switch (row.getType()) {
                case "PHOTO":
                    Log.d("Current user Input", "ID : " + row.getId() + " User Input Photo : " + row.getDataMap().getBitmap());
                    break;
                case "SINGLE_CHOICE":
                    Log.d("Current user Input", "ID : " + row.getId() + " User Input Choice : " + row.getDataMap().getOptionSelection());
                    break;
                case "COMMENT":
                    Log.d("Current user Input", "ID : " + row.getId() + " User Input Comment : " + row.getDataMap().getComment());
                    break;
            }

        }
    }
}
