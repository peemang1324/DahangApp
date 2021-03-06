package com.example.sns_project.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sns_project.fragment.ChatFragment;
import com.example.sns_project.fragment.HomeFragment;
import com.example.sns_project.fragment.ProfileFragment;
import com.example.sns_project.fragment.SearchFragment;
import com.example.sns_project.info.PostInfo;
import com.example.sns_project.R;
import com.example.sns_project.adapter.PostAdapter;
import com.example.sns_project.listener.OnPostListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import static com.example.sns_project.Util.isStorageUrl;
import static com.example.sns_project.Util.showToast;
import static com.example.sns_project.Util.storageUrlToName;


/*
TODO 1. 채팅 프로필 이미지 변경시 storage 저장 통합 (o)
     2. 툴바 만들기
     3. 게시판 검색기능, 사용자 이름 나오게 수정
     4. 관광 api 통합하기 + 구글 map API 등록하기
     5. 사용자 정보 구체화 DB저장
 */

public class MainActivity extends BasicActivity {
    private long backPressedTime; // 두번눌러서 앱종료
    private Toast backToast; // Toast value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 여기부터 bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                new HomeFragment()).commit();

    }

    // bottom navigation 에서 해당하는 fragment 호출 함수
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;

                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;

                    case R.id.nav_chat:
                        selectedFragment = new ChatFragment();
                        break;

                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                        selectedFragment).commit();

                return true;
            };

    // 두 번 눌러서 창을 나가기 위해 시간 설정 함수
    @Override
    public void onBackPressed(){

        if (backPressedTime + 2000 > System.currentTimeMillis()){ // 일정시간 이내에 두번 클릭인지 측정
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

}