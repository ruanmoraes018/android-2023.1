package com.android.tokentravel;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Passageiro;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FormCadastro extends AppCompatActivity {
    View containnermotora;
    EditText editTextNome, editTextCPF, editTextEmail, editTextSenha, editTextTelefone, editTextCnh, editTextModeloCarro, editTextPlacaVeiculo;
    Spinner spinnerTipo;
    Button botao;
    public boolean nomePreenchido = false, cpfPreenchido = false, emailPreenchido = false, senhaPreenchida = false, telefonePreenchido = false, cnhPreenchida = false, modeloCarroPreenchido = false, placaVeiculoPreenchida = false, senhaVisivel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        botao = findViewById(R.id.bt_cadastrar);
        editTextNome = findViewById(R.id.edit_nome);
        editTextCPF = findViewById(R.id.edit_cpf);
        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);
        spinnerTipo = findViewById(R.id.spinner_user_type);
        editTextTelefone = findViewById(R.id.edit_telefone);
        editTextCnh = findViewById(R.id.edit_cnh);
        editTextModeloCarro = findViewById(R.id.edit_modeloCarro);
        editTextPlacaVeiculo = findViewById(R.id.edit_placaVeiculo);
        containnermotora = findViewById(R.id.containerComponentsmotora);
        ImageView imageViewEye; // Ícone de olho
        imageViewEye = findViewById(R.id.imageViewEye); // Referência ao ícone de olho
        editTextCPF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                editTextCPF.removeTextChangedListener(this);
                String formattedCPF = formatCPF(s.toString());
                editTextCPF.setText(formattedCPF);
                editTextCPF.setSelection(formattedCPF.length());
                editTextCPF.addTextChangedListener(this);
            }
        });
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (isValidEmail(email)) {
                    emailPreenchido = true;
                } else {
                    emailPreenchido = false;
                }
            }
        });
        imageViewEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senhaVisivel = !senhaVisivel;
                if (senhaVisivel) {
                    editTextSenha.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    editTextSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editTextSenha.setSelection(editTextSenha.getText().length());
            }
        });
        editTextTelefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString();
                String formattedPhoneNumber = formatPhoneNumber(phoneNumber);
                editTextTelefone.removeTextChangedListener(this);
                editTextTelefone.setText(formattedPhoneNumber);
                editTextTelefone.setSelection(formattedPhoneNumber.length());
                editTextTelefone.addTextChangedListener(this);
            }
        });
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // strm o tipo de pessoa selecionado
                String tipoPessoa = parent.getItemAtPosition(position).toString();
                if (tipoPessoa.equals("Motorista")) {
                    animateViewVisibility(editTextCnh, View.VISIBLE);
                    animateViewVisibility(editTextModeloCarro, View.VISIBLE);
                    animateViewVisibility(editTextPlacaVeiculo, View.VISIBLE);
                    animateViewVisibility(containnermotora, View.VISIBLE);
                } else {
                    animateViewVisibility(editTextCnh, View.GONE);
                    animateViewVisibility(editTextModeloCarro, View.GONE);
                    animateViewVisibility(editTextPlacaVeiculo, View.GONE);
                    animateViewVisibility(containnermotora, View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        String[] userTypes = {"Passageiro", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userTypes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Defina a cor do texto aqui
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Defina a cor do texto aqui para o drop-down
                return view;
            }
        };
        spinnerTipo.setAdapter(adapter);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tipoPessoa = spinnerTipo.getSelectedItem().toString();
                String email = editTextEmail.getText().toString();
                String tipo = spinnerTipo.getSelectedItem().toString();
                Dao dao = new Dao(getApplicationContext());
                String resultadoEmail;
                try {
                    resultadoEmail = dao.buscaPessoaEmail(email);
                } catch (Exception e) {
                    Log.e("Erro", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Erro ao verificar o email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String cpf = editTextCPF.getText().toString();
                String resultadoCPF;
                try {
                    resultadoCPF = dao.buscaPessoaCPF(cpf);
                } catch (Exception e) {
                    Log.e("Erro", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Erro ao verificar o CPF.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isCPFValido(cpf)) {
                    Toast.makeText(getApplicationContext(), "CPF inválido!", Toast.LENGTH_SHORT).show();
                    return; // Impede o cadastro se o CPF for inválido
                }
                if (resultadoEmail != null) {
                    Toast.makeText(getApplicationContext(), "O email já está cadastrado.", Toast.LENGTH_SHORT).show();
                } else if (resultadoCPF != null) {
                    Toast.makeText(getApplicationContext(), "O CPF já está cadastrado.", Toast.LENGTH_SHORT).show();
                } else if (tipo.equals("Passageiro")){
                    if (TextUtils.isEmpty(editTextNome.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        nomePreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextCPF.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O CPF é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        cpfPreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        emailPreenchido = true;
                    }
                    String email1 = editTextEmail.getText().toString().trim();
                    if (!isValidEmail(email1)) {
                        Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(editTextSenha.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "A senha é obrigatória.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        senhaPreenchida = true;
                    }
                    if (TextUtils.isEmpty(editTextTelefone.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O telefone é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        telefonePreenchido = true;
                    }
                    View containerComponents = findViewById(R.id.containerComponents);
                    containerComponents.getLayoutParams().height = 1040;
                    containerComponents.requestLayout();
                    cadastrarPassageiro();
                    Toast.makeText(getApplicationContext(), "Passageiro cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                if (tipoPessoa.equals("Motorista")) {
                    if (TextUtils.isEmpty(editTextNome.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        nomePreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextCPF.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O CPF é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        cpfPreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        emailPreenchido = true;
                    }
                    String email1 = editTextEmail.getText().toString().trim();
                    if (!isValidEmail(email1)) {
                        Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(editTextSenha.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "A senha é obrigatória.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        senhaPreenchida = true;
                    }
                    if (TextUtils.isEmpty(editTextTelefone.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O telefone é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        telefonePreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextCnh.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "A CNH é obrigatória.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        cnhPreenchida = true;
                    }
                    if (TextUtils.isEmpty(editTextModeloCarro.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "O modelo do carro é obrigatório.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        modeloCarroPreenchido = true;
                    }
                    if (TextUtils.isEmpty(editTextPlacaVeiculo.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "A placa do veículo é obrigatória.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        placaVeiculoPreenchida = true;
                    }
                    View containerComponents = findViewById(R.id.containerComponents);
                    containerComponents.getLayoutParams().height = 1500;
                    containerComponents.requestLayout();
                    cadastrarMotorista();
                    Toast.makeText(getApplicationContext(), "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String formatCPF(String cpf) {
        String cleanCPF = cpf.replaceAll("[^\\d]", "");
        return cleanCPF.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
    // Função para verificar se o CPF é válido
    // Função para verificar se o CPF é válido
    private boolean isCPFValido(String cpf) {
        // Remove caracteres não numéricos do CPF
        String cleanCPF = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cleanCPF.length() != 11) {
            return false;
        }

        // Calcula o primeiro dígito verificador do CPF
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cleanCPF.charAt(i)) * (10 - i);
        }
        int resto = 11 - (soma % 11);
        int digitoVerificador1 = (resto == 10 || resto == 11) ? 0 : resto;

        // Calcula o segundo dígito verificador do CPF
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cleanCPF.charAt(i)) * (11 - i);
        }
        resto = 11 - (soma % 11);
        int digitoVerificador2 = (resto == 10 || resto == 11) ? 0 : resto;

        // Verifica se os dígitos verificadores calculados coincidem com os dígitos do CPF
        return digitoVerificador1 == Character.getNumericValue(cleanCPF.charAt(9)) &&
                digitoVerificador2 == Character.getNumericValue(cleanCPF.charAt(10));
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private String formatPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        if (digitsOnly.length() == 11) {
            return "(" + digitsOnly.substring(0, 2) + ") " + digitsOnly.substring(2, 7) + "-" + digitsOnly.substring(7);
        } else {
            return digitsOnly;
        }
    }
    private void animateViewVisibility(View view, int visibility) {
        view.setVisibility(visibility);
        Animation animation = visibility == View.VISIBLE ?
                AnimationUtils.loadAnimation(this, R.anim.slide_down) :
                AnimationUtils.loadAnimation(this, R.anim.slide_up);
        view.startAnimation(animation);
    }
    private void cadastrarPassageiro() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String telefone = editTextTelefone.getText().toString();
        Passageiro passageiro = new Passageiro(nome, cpf, email, senha,  telefone, tipo, null);
        Dao dao = new Dao(this);
        String resultado = dao.inserirPassageiro(passageiro);
        Log.d("Resultado: ", resultado);
        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);

    }
    private void cadastrarMotorista() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String telefone = editTextTelefone.getText().toString();
        String cnh = editTextCnh.getText().toString();
        String modelo_carro = editTextModeloCarro.getText().toString();
        String placa_veiculo = editTextPlacaVeiculo.getText().toString();

        Motorista motorista = new Motorista(nome, cpf, email, senha, telefone, tipo, cnh, modelo_carro, placa_veiculo, null);
        Dao dao = new Dao(this);
        String resultado = dao.inserirMotorista(motorista);
        Log.d("Resultado: ", resultado);
        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);

    }
}