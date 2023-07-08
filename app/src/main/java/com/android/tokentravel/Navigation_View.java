package com.android.tokentravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Stack;

public class Navigation_View extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Button botaoPesquisarRota;

    private Button btpesquisaRota;


    private Stack<Fragment> fragmentStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        botaoPesquisarRota = findViewById(R.id.botao_pesquisar_rota);
        fragmentStack = new Stack<>();
        IniciarBotPesquisaRota();



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Meus Dados", Toast.LENGTH_SHORT).show();
                    fragmentR(new MenuFragment());
                    botaoPesquisarRota.setVisibility(View.GONE); // Torna o botão visível
                } else if (itemId == R.id.historico) {
                    fragmentR(new HistoricoFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Histórico", Toast.LENGTH_SHORT).show();
                    botaoPesquisarRota.setVisibility(View.GONE); // Torna o botão visível
                } else if (itemId == R.id.suporte) {
                    fragmentR(new SuporteFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Fale Conosco", Toast.LENGTH_SHORT).show();
                    botaoPesquisarRota.setVisibility(View.GONE); // Torna o botão visível
                } else {
                    // Item de deslogar da aplicação.
                }

                return true;
            }
        });

        botaoPesquisarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation_View.this, PesquisaRota.class);
                startActivity(intent);
            }
        });

        btpesquisaRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation_View.this, PesquisaRota.class);
                startActivity(intent);
            }
        });

    }

    private void fragmentR(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentStack.push(fragment);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!fragmentStack.isEmpty()) {
            fragmentStack.pop();

            if (!fragmentStack.isEmpty()) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentStack.clear();
                botaoPesquisarRota.setVisibility(View.VISIBLE); // Torna o botão visível

            } else {
                super.onBackPressed();
                botaoPesquisarRota.setVisibility(View.VISIBLE); // Torna o botão visível

            }
        } else {
            super.onBackPressed();
        }
    }
    private void IniciarBotPesquisaRota()
    {
        btpesquisaRota = findViewById(R.id.botao_pesquisar_rota);

    }

}
