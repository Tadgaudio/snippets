package com.alternativasoftware.ibs.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alternativasoftware.ibs.R;

import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.controller.adapters.CidadeAdapter;
import com.alternativasoftware.ibs.controller.adapters.EstadosAdapter;
import com.alternativasoftware.ibs.controller.async.EnviarEntrevista;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.COM_Cliente;
import com.alternativasoftware.ibs.model.classes.COM_Estado;
import com.alternativasoftware.ibs.model.classes.COM_Municipio;
import com.alternativasoftware.ibs.model.classes.COM_Produtor;
import com.alternativasoftware.ibs.model.classes.COM_Propriedade;
import com.alternativasoftware.ibs.model.classes.ENT_Entrevista;
import com.alternativasoftware.ibs.model.classes.ENT_Formulario;
import com.alternativasoftware.ibs.model.classes.ENT_StatusEntrevista;
import com.alternativasoftware.ibs.model.dao.COM_ClienteDao;
import com.alternativasoftware.ibs.model.dao.COM_EstadoDao;
import com.alternativasoftware.ibs.model.dao.COM_MunicipioDao;
import com.alternativasoftware.ibs.model.dao.COM_ProdutorDao;
import com.alternativasoftware.ibs.model.dao.COM_PropriedadeDao;
import com.alternativasoftware.ibs.model.dao.ENT_EntrevistaDao;
import com.alternativasoftware.ibs.model.dao.ENT_FormularioDao;
import com.alternativasoftware.ibs.model.dao.ENT_StatusEntrevistaDao;
import com.alternativasoftware.ibs.view.EntrevistasActivity;
import com.alternativasoftware.ibs.view.FormularioActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tadeu.gaudio on 20/08/13.
 */
public class DetalhesEntrevista extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "DetalhesEntrevista";
    private static int entrevistaID;

    private EditText edNomePropriedade;
    private EditText edPropriedadeCNPJ;
    private EditText edCPF;
    private EditText edPropriedadeTelefone;
    private EditText edPropriedadeBairro;
    private ENT_Entrevista entrevista;
    private COM_Propriedade propriedade;


    private TextView txtHeaderCliente;
    private TextView txtHeaderFormulario;
    private TextView txtHeaderProgresso;
    private TextView txtHeaderStatus;
    private COM_Cliente cliente;
    private ENT_StatusEntrevista status;
    private ENT_Formulario formulario;

    private Context context;
    private List<COM_Estado> estados;
    private List<COM_Municipio> cidades;
    private Spinner spinnerEstado;
    private EstadosAdapter estadosAdapter;
    private Button btIniciarEntrevista;
    private Button btEnviarEntrevista;
    private Button btSalvarDados;
    private Spinner spinnerCidade;
    private CidadeAdapter cidadeAdapter;

    private Global globalVars;
    private EditText edDDD;


    public static DetalhesEntrevista newInstance(int index) {
        DetalhesEntrevista f = new DetalhesEntrevista();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public void setArguments(int entrevistaID){
       this.entrevistaID = entrevistaID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalhes_entrevista, container, false);

        if(entrevistaID > 0)
            Log.i(TAG, "EntrevistaID: "+entrevistaID);

        context = getActivity().getApplicationContext();
        globalVars = (Global) context;


        //Seta as ações nos botões da interface de detalhes da entrevista.
        btIniciarEntrevista = (Button) view.findViewById(R.id.iniciar_entrevista);
        btIniciarEntrevista.setOnClickListener(this);

        btEnviarEntrevista = (Button) view.findViewById(R.id.btEnviarEntrevista);
        btEnviarEntrevista.setOnClickListener(this);

        btSalvarDados = (Button) view.findViewById(R.id.btSalvarDados);
        btSalvarDados.setOnClickListener(this);

        /* Spinner de opções */
        DBAdapter db = new DBAdapter(context);
        db.open();
            entrevista = ENT_EntrevistaDao.getEntrevista(entrevistaID);
            cliente = COM_ClienteDao.getCliente(entrevista.get_ClienteId());
            propriedade = COM_PropriedadeDao.getPropriedade(entrevista.get_PropriedadeId());
            status = ENT_StatusEntrevistaDao.getStatusEntrevista(entrevista.get_StatusEntrevistaId());
            formulario = ENT_FormularioDao.getFormulario(entrevista.get_FormularioId());
            estados = COM_EstadoDao.getEstados();
            cidades = COM_MunicipioDao.getMunicipios();
        db.close();


        spinnerEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        estadosAdapter = new EstadosAdapter(getActivity().getApplicationContext(), estados);
        spinnerEstado.setAdapter(estadosAdapter);
        spinnerEstado.setOnItemSelectedListener(this);

        spinnerCidade = (Spinner) view.findViewById(R.id.spinnerCidade);
        cidadeAdapter = new CidadeAdapter(getActivity().getApplicationContext(), cidades);
        spinnerCidade.setAdapter(cidadeAdapter);
        spinnerCidade.setOnItemSelectedListener(this);

        //cabeçalho superior
        txtHeaderCliente = (TextView) view.findViewById(R.id.txtHeaderCliente);
        txtHeaderFormulario = (TextView) view.findViewById(R.id.txtHeaderFormulario);
        txtHeaderProgresso = (TextView) view.findViewById(R.id.txtHeaderProgresso);
        txtHeaderStatus = (TextView) view.findViewById(R.id.txtHeaderStatus);

        //conteúdo do cadastro
        edNomePropriedade = (EditText) view.findViewById(R.id.edNomePropriedade);
        edPropriedadeCNPJ = (EditText) view.findViewById(R.id.edPropriedadeCNPJ);
        edCPF = (EditText) view.findViewById(R.id.edCPF);
        edPropriedadeTelefone = (EditText) view.findViewById(R.id.edPropriedadeTelefone);
        edDDD = (EditText) view.findViewById(R.id.edDDD);
        edPropriedadeBairro = (EditText) view.findViewById(R.id.edPropriedadeBairro);

        globalVars.setEntrevista(entrevista);
        txtHeaderCliente.setText(cliente.get_Nome());
        txtHeaderFormulario.setText(formulario.get_Descricao());

        /* progresso é medido de acordo com a quantidade de perguntas
           obrigatórias respondidas.
         */
        int progresso = (int) entrevista.get_PercConcluida();
        txtHeaderProgresso.setText(String.valueOf(progresso)+"%");
        txtHeaderStatus.setText(status.get_Descricao());

        edNomePropriedade.setText(propriedade.get_Nome());
        edPropriedadeCNPJ.setText(propriedade.get_Cnpj());
        edCPF.setText(propriedade.get_CpfProdutor());

        String ddd = propriedade.get_Telefone().substring(0, 2);
        String fone = propriedade.get_Telefone().toString().substring(2, 10);
        edDDD.setText(ddd);
        edPropriedadeTelefone.setText(fone);

        edPropriedadeBairro.setText(propriedade.get_Bairro());

        /*
            =================================
              Setar estados
            ================================
         */

        // primeiro vamos verificar se a entrevista foi realizada e enviada
        // caso positivo o botão enviar vai ficar desabilitado.
        if( status.get_Sigla().equals("EN") )
            btEnviarEntrevista.setEnabled(false);

        if(entrevista.get_QtdeObrigatoriasRespondidas() == entrevista.get_QtdePerguntasObrigatorias()){
            btEnviarEntrevista.setEnabled(true);
            btEnviarEntrevista.setText("ENVIAR");
            txtHeaderStatus.setText("Realizada");
            txtHeaderProgresso.setTextColor(getResources().getColor(R.color.texto_verde));
        }

        txtHeaderProgresso.setText(String.valueOf((int) entrevista.get_PercConcluida())+"%");

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iniciar_entrevista:
                abrirForumlario();
                break;
            case R.id.btEnviarEntrevista:
                enviarEntrevista();
                break;
            case R.id.btSalvarDados:
                atualizarCadastro();
                break;
        }
    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    /**
     * O método de atualizar cadastro salva as informações do consultor dentro do tablet. Para poder
     * atualizar o cadastro do usuário no CMS é necessário transmitir a entrevista completa através
     * da sincronização.
     */
    private void atualizarCadastro() {

        COM_Propriedade prop = new COM_Propriedade();

        prop.set_Nome(edNomePropriedade.getText().toString());
        prop.set_Cnpj(edPropriedadeCNPJ.getText().toString());
        prop.set_CpfProdutor(edCPF.getText().toString());
        prop.set_Telefone(edDDD.getText()+edPropriedadeTelefone.getText().toString());
        prop.set_Bairro(edPropriedadeBairro.getText().toString());

        View v = (View) spinnerCidade.getSelectedView();
        prop.set_MunicipioId(v.getId());

        prop.set_GrupoId(entrevista.get_GrupoId());
        prop.set_Ativo(true);

        prop.set_OrigemId(propriedade.get_OrigemId());

        DBAdapter db = new DBAdapter(context);
        db.open();
            COM_PropriedadeDao.save(prop);
        db.close();

        Toast.makeText(getActivity(), R.string.dadosPropriedadeAtualizados, Toast.LENGTH_SHORT).show();
    }


    public void enviarEntrevista(){
        ENT_Entrevista entrevista = globalVars.getEntrevista();
        EnviarEntrevista enviar = new EnviarEntrevista(getActivity());
        enviar.execute(entrevista.get_Id());
    }


    /**
     * Abre a activity de formulário
     */
    protected void abrirForumlario(){
        Toast.makeText(getActivity(), "Iniciar entrevista", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(getActivity(), FormularioActivity.class);

        it.putExtra("entrevista", entrevista);
        it.putExtra("consultor", globalVars.getConsultor());
        it.putExtra("cliente", cliente);
        it.putExtra("formulario", formulario);

        startActivity(it);

    }
}
