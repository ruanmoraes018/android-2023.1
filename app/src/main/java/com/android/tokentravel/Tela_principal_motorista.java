package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class Tela_principal_motorista extends AppCompatActivity {

    private ImageButton menuButton;
    private Button buttonverrota;
    private Button buttoncadastar_rota;
    private boolean isCadastrarRotaFragmentLoaded = false;
    private boolean isMenuButtonEnabled = true; // Variável para controlar o estado do botão de menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_motorista);

        menuButton = findViewById(R.id.menuButton);
        buttonverrota = findViewById(R.id.button2);
        buttoncadastar_rota = findViewById(R.id.button1);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        buttonverrota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma instância do fragmento VerRotasFragment
                VerRotasFragment verRotasFragment = new VerRotasFragment();

                // Iniciar uma transação de fragmento
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Substituir o fragmento atual pelo VerRotasFragment
                transaction.replace(R.id.fragment_cadastrarrota, verRotasFragment);

                // Adicionar a transação à pilha de fragmentos (opcional)
                // transaction.addToBackStack(null);

                // Confirmar a transação
                transaction.commit();
            }
        });

        buttoncadastar_rota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCadastrarRotaFragment();
            }
        });



        loadCadastrarRotaFragment();
    }

    private void showPopupMenu(View view) {
        if (!isMenuButtonEnabled) {
            return; // Não exibe o menu se o botão estiver desabilitado
        }

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_motora); // Infla o arquivo de menu

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // Verifique se o fragmento CadastrarFragment está carregado e visível
                Fragment cadastrarRotaFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_cadastrarrota);
                if (cadastrarRotaFragment != null && cadastrarRotaFragment.isVisible()) {
                    // Oculta o fragmento CadastrarFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(cadastrarRotaFragment)
                            .commit();
                    isCadastrarRotaFragmentLoaded = false;
                }

                if (item.getItemId() == R.id.menu) {
                    MenuMotoraFragment menuMotoraFragment = new MenuMotoraFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_MotoraMenu, menuMotoraFragment)
                            .addToBackStack(null)
                            .commit();
                    isMenuButtonEnabled = false; // Desativa o botão quando o fragmento é carregado
                    return true;
                } else if (item.getItemId() == R.id.suporte) {
                    SuporteFragment menuSuporteMotoFragment = new SuporteFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_SuporteMenu, menuSuporteMotoFragment)
                            .addToBackStack(null)
                            .commit();
                    isMenuButtonEnabled = false; // Desativa o botão quando o fragmento é carregado
                    return true;
                }
                else if (item.getItemId() == R.id.deslogar) {
                    // Implemente a ação para o item "Deslogar" aqui
                    // Exemplo: finish(); // Encerra a atividade atual
                    loadCadastrarRotaFragment(); // Talvez precise remover essa linha, quando deslocar. Por enquanto ela fica aqui.
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void loadCadastrarRotaFragment() {
        CadastrarFragment cadastrarRotaFragment = new CadastrarFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_cadastrarrota, cadastrarRotaFragment)
                .commit();
        isCadastrarRotaFragmentLoaded = true;
    }

    @Override
    public void onBackPressed() {
        // Verifica se há fragmentos na pilha de fragmentos
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Se houver fragmentos, reativa o botão de menu
            isMenuButtonEnabled = true;
            loadCadastrarRotaFragment();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
