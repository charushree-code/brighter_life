package com.example.brighterlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brighterlife.auth.Login;
import com.example.brighterlife.auth.Register;
import com.example.brighterlife.breathe.breathe_activity;
import com.example.brighterlife.modal.Adapter;
import com.example.brighterlife.modal.Page;
import com.example.brighterlife.page.addnotes;
import com.example.brighterlife.page.editPage;
import com.example.brighterlife.page.journal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
DrawerLayout drawerLayout;
ActionBarDrawerToggle toggle;
NavigationView nav_view;
RecyclerView journalList;
FirebaseFirestore fStore;
FirestoreRecyclerAdapter<Page,PageViewHolder> Pageadapter;
FirebaseUser user;
FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fStore=FirebaseFirestore.getInstance();
      fAuth=FirebaseAuth.getInstance();
      user=fAuth.getCurrentUser();



        Query query=fStore.collection("Journal").document(user.getUid()).collection("myPages").orderBy("date", Query.Direction.DESCENDING);


        //Journal>>uuid>>myPages>>title,content,imageId
        FirestoreRecyclerOptions<Page> allPages = new FirestoreRecyclerOptions.Builder<Page>().setQuery(query,Page.class).build();
        Pageadapter =new FirestoreRecyclerAdapter<Page, PageViewHolder>(allPages) {
            @Override
            protected void onBindViewHolder(@NonNull PageViewHolder pageViewHolder, int i, @NonNull Page page) {
                pageViewHolder.noteTitle.setText(page.getTitle());
                pageViewHolder.noteContent.setText(page.getContent());
              Integer image= imagesetting(page.getImageId());
              pageViewHolder.imageView.setImageResource(image);
                Integer code =getRandomcolor();
                pageViewHolder.cardView.setCardBackgroundColor(pageViewHolder.view.getResources().getColor(code,null));
                String docId=Pageadapter.getSnapshots().getSnapshot(i).getId();
                pageViewHolder.view.setOnClickListener((v)-> {
                    Intent ii =new Intent(v.getContext(), journal.class);
                    ii.putExtra("title",page.getTitle());
                    ii.putExtra("content",page.getContent());
                    ii.putExtra("code",code);
                    ii.putExtra("feelings",image);
                    ii.putExtra("pageId",docId);
                    v.getContext().startActivity(ii);

                });


               ImageView menuIcon=pageViewHolder.view.findViewById(R.id.menuIcon);
               menuIcon.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       final String docId1 =Pageadapter.getSnapshots().getSnapshot(i).getId();
                       PopupMenu menu=new PopupMenu(v.getContext(),v);
                       menu.setGravity(Gravity.END);
                       menu.getMenu().add("edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {

                               Intent iq =new Intent(v.getContext(), editPage.class);
                               iq.putExtra("title",page.getTitle());
                               iq.putExtra("content",page.getContent());
                               iq.putExtra("feelings",page.getImageId());
                               iq.putExtra("pageId",docId1);
                               startActivity(iq);
                               return false;
                           }
                       });
                       menu.getMenu().add("delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem item) {
                               DocumentReference docref = fStore.collection("Journal").document(user.getUid()).collection("myPages").document(docId1);
                              docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      //note Deleted
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                                  }
                              });
                               return false;
                           }
                       });
                        menu.show();
                   }
               });
            }

            @NonNull
            @Override
            public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal,parent,false);
                return new PageViewHolder(view);
            }
        };



        journalList =findViewById(R.id.journalList);

        drawerLayout=findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
    toggle.syncState();






        journalList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        journalList.setAdapter(Pageadapter);



        View headerView = nav_view.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userDisplayName);
        TextView userEmail = headerView.findViewById(R.id.userDisplayEmail);

        if(user.isAnonymous()){
            userEmail.setVisibility(View.GONE);
            username.setText("Temporary User");
        }else {
            userEmail.setText(user.getEmail());
            username.setText(user.getDisplayName());
        }










        FloatingActionButton fab = findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), addnotes.class));
            }});
        FloatingActionButton start = findViewById(R.id.start_breathe);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), breathe_activity.class));
            }});



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.add:
                startActivity(new Intent(this,addnotes.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.sync:
                if(user.isAnonymous())
                {startActivity(new Intent(this, Login.class));
                    overridePendingTransition(R.anim.slide_up,R.anim.slide_down);}
                else
                    {
                        Toast.makeText(this,"Your already connected with a account",Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.logout:
               // FirebaseAuth.getInstance().signOut();
                checkUser();
                break;
            default:
                Toast.makeText(this,"comming soon",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

public class PageViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView noteTitle,noteContent;
    View view;
    CardView cardView;
    public PageViewHolder(@NonNull View itemView) {
        super(itemView);



        noteTitle=itemView.findViewById(R.id.titles);
        noteContent=itemView.findViewById(R.id.content);
        cardView=itemView.findViewById(R.id.noteCard);
        imageView=itemView.findViewById(R.id.imageView);
        view =itemView;
    }
}

    public int imagesetting(int a){


        int image=R.drawable.happy;
        if (a==0)
            image=R.drawable.frustrated;
        if (a==1)
            image=R.drawable.happy;
        if (a==2)
            image=R.drawable.sad;
        if (a==3)
            image=R.drawable.stressed;

        return image;
    }
    private int getRandomcolor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.red);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Pageadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Pageadapter !=null){
            Pageadapter.stopListening();
        }
    }
    private void checkUser(){
        if(user.isAnonymous()){
            displayAlert();
        }else{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),splash.class));
            overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            finish();
        }
    }

    private void displayAlert() {
        AlertDialog.Builder warning = new AlertDialog.Builder(this)
        .setTitle("Are you sure?")
        .setMessage("you have logged in with temporary account ,Logging out will delete all your records")
        .setPositiveButton("Sync Pages", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                finish();
            }
        }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete all pages

                //delete anamolous user

                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),splash.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        warning.show();
    }
}