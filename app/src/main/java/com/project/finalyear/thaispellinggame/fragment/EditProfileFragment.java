package com.project.finalyear.thaispellinggame.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.finalyear.thaispellinggame.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    private CircleImageView edit_profile_image;
    Button editProfileSuccess;
    EditText editUsername;
    EditText editEmail;
    Button btnSave;

    Context context;

    private final static int Gallery_Pick = 1;
    private StorageReference storeProfileImagestorageRef;

    private DatabaseReference getUserDatabaseRef;
    private FirebaseAuth mAuth;
    String nameEdit;
    String emailEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        initInstance(view);
        return view;
    }

    private void initInstance(View view) {

        mAuth = FirebaseAuth.getInstance();
        String online_user_id = mAuth.getCurrentUser().getUid();
        getUserDatabaseRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(online_user_id);
        storeProfileImagestorageRef = FirebaseStorage.getInstance().getReference().child("Profile_Images");


        edit_profile_image = (CircleImageView) view.findViewById(R.id.edit_profile_image);
        editUsername = (EditText) view.findViewById(R.id.editUsername);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        btnSave = (Button) view.findViewById(R.id.btnSaveEditProfile);
        //btn_change_image = (Button) view.findViewById(R.id.change_image_button);


        getUserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                // get data show to edit text
                editUsername.setText(name);
                editEmail.setText(email);
                editUsername.setSelection(editUsername.getText().length());
                editEmail.setSelection(editEmail.getText().length());

                    if (image.equals("default_profile_pic")) {

                        Picasso.with(getContext()).load(R.drawable.default_profile_pic).resize(150, 150)
                                .centerCrop().into(edit_profile_image);

                    } else {
                        Picasso.with(getContext()).load(image).resize(150, 150)
                                .centerCrop().into(edit_profile_image);
                    }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editUsername.getText().toString();
                String email = editEmail.getText().toString();

                editUsername.setSelection(editUsername.getText().length());
                editEmail.setSelection(editEmail.getText().length());

                // set data to firebase
                if (name.isEmpty()) {
                    getUserDatabaseRef.child("name").setValue(name);
                } else if (email.isEmpty()) {
                    getUserDatabaseRef.child("email").setValue(email);
                } else if (name.isEmpty() && email.isEmpty()) {
                    getUserDatabaseRef.child("name").setValue(name);
                    getUserDatabaseRef.child("email").setValue(email);

                }else {
                    getUserDatabaseRef.child("name").setValue(name);
                    getUserDatabaseRef.child("email").setValue(email);
                }
                // show dialog success edit profile
                dialogProfile();


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filePath = storeProfileImagestorageRef.child(user_id + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "กำลังบันทึกรูปภาพโปรไฟล์...", Toast.LENGTH_SHORT).show();

                            String downloadUri = task.getResult().getDownloadUrl().toString();
                            getUserDatabaseRef.child("image").setValue(downloadUri)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "อัปเดตรูปภาพโปรไฟล์สำเร็จ!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "เกิดข้อผิดพลาดขณะอัปโหลดรูปภาพโปรไฟล์", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void dialogProfile() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_popup_edit_profile);
        dialog.show();
        editProfileSuccess = (Button) dialog.findViewById(R.id.editProfileSuccess);
        editProfileSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
}
