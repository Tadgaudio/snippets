package com.alternativasoftware.ibs.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alternativasoftware.ibs.R;
import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.controller.adapters.ListaGrupoPerguntasAdapter;
import com.alternativasoftware.ibs.controller.adapters.ListaPerguntasAdapter;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.ENT_GrupoPergunta;
import com.alternativasoftware.ibs.model.classes.ENT_Pergunta;

import com.alternativasoftware.ibs.model.dao.ENT_PerguntaDao;
import com.alternativasoftware.ibs.util.AndroidUtils;
import com.alternativasoftware.ibs.util.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe extende a classe de fragmento trabalha a lista de grupo de perguntas e as ações que
 * são necessárias para que o consultor possa navegar entre as perguntas dentro de cada item do
 * Grupo de Perguntas.
 *
 * @see com.alternativasoftware.ibs.controller.adapters.ListaGrupoPerguntasAdapter
 *
 * Created by tadeu.gaudio on 20/08/13.
 */
public class DetalhesGrupoPerguntas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "DetalhesGrupoPerguntas";
    private List<ENT_Pergunta> lista_perguntas_do_grupo = new ArrayList<ENT_Pergunta>();
    private ListaPerguntasAdapter adapter;
    private int grupo_de_perguntas_id;
    private String tituloGrupoPerguntaSelecionado;
    private Button anterior;
    private Button proxima;
    private ListView lstPerguntas;
    private ListView lstGrupoPerguntas;
    private Context context;
    private Global globalVars;
    private ListaGrupoPerguntasAdapter adapterGrupo;
    private Button proximoGrupoPerguntas;
    private Button grupoPerguntasAnterior;


    public void setArguments(int grupo_de_perguntas_id){
        this.grupo_de_perguntas_id = grupo_de_perguntas_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.detalhes_formulario, container, false);

        this.context = getActivity().getApplicationContext();
        this.globalVars = (Global) context;

        DBAdapter db = new DBAdapter(context);
        db.open();
            lista_perguntas_do_grupo = ENT_PerguntaDao.getPerguntasByGrupoId(grupo_de_perguntas_id);
        db.close();

        ENT_GrupoPergunta grupo_selecionado = globalVars.getGrupoPerguntasSelecionada();
        tituloGrupoPerguntaSelecionado = grupo_selecionado.get_Titulo();

        TextView txtNumQuestoes = (TextView) view.findViewById(R.id.txtNumeroQuestoes);
        txtNumQuestoes.setText(String.valueOf(lista_perguntas_do_grupo.size())+" "+getString(R.string.questoes));

        TextView txtTituloDoGrupodePerguntas = (TextView) view.findViewById(R.id.txtTituloGrupoPerguntaSelecionado);
        txtTituloDoGrupodePerguntas.setText(tituloGrupoPerguntaSelecionado);


        adapter = new ListaPerguntasAdapter(getActivity(), lista_perguntas_do_grupo);
        lstPerguntas = (ListView) view.findViewById(R.id.listaPerguntas);

        lstPerguntas.getAdapter();
        lstPerguntas.setAdapter(adapter);


        lstGrupoPerguntas = (ListView) getActivity().findViewById(android.R.id.list);
        lstGrupoPerguntas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        proximoGrupoPerguntas = (Button) view.findViewById(R.id.btProximoGrupo);
        grupoPerguntasAnterior = (Button) view.findViewById(R.id.btGrupoAnterior);
        adapterGrupo = (ListaGrupoPerguntasAdapter) lstGrupoPerguntas.getAdapter();

        proximoGrupoPerguntas.setOnClickListener(this);
        grupoPerguntasAnterior.setOnClickListener(this);


        proxima = (Button) view.findViewById(R.id.btProxima);
        anterior = (Button) view.findViewById(R.id.btAnterior);

        proxima.setOnClickListener(this);
        anterior.setOnClickListener(this);

        Button btSalvarESair = (Button) view.findViewById(R.id.btSalvarSair);
        btSalvarESair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.gravarEstadoFormulario(context);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btProxima:
                try {
                    trocarPergunta(true);
                } catch (IOException e) {
                    Log.e(TAG, "erro ao trocar para próxima pergunta");
                }

                break;
            case R.id.btAnterior:
                try {
                    trocarPergunta(false);
                } catch (IOException e) {
                    Log.e(TAG, "erro ao trocar para pergunta anterior");
                }
                break;
            case R.id.btProximoGrupo:
                try{

                    trocarGrupoPergunta(true);
                }catch (IOException e) {
                    Log.e(TAG, "erro ao trocar para o próximo grupo pergunta");
                }
                break;
            case R.id.btGrupoAnterior:
                try{
                    trocarGrupoPergunta(false);
                }catch (IOException e) {
                    Log.e(TAG, "erro ao trocar para o grupo de perguntas anterior");
                }
                break;
        }
    }


    private void trocarPergunta(boolean proxima) throws IOException {
        Log.e(TAG, "trocar pergunta");
        if(lstPerguntas != null){
            // todo @continue: trocarPergunta, precisa sinalizar o item que mudou além de configurar a variável global adequadamente
            if(adapter == null )
                adapter = (ListaPerguntasAdapter) lstPerguntas.getAdapter();

            if(proxima){
                int index = adapter.getCurrentPosition();
                if(index < adapter.getCount()-1){ // aqui fazemos menos 1 pois getCount começa no 0 e o incrementador não
                    index += 1;
                    adapter.getView(index, null, null).performClick();
                }else
                    Toast.makeText(context, R.string.ultima_pergunta_respondida, Toast.LENGTH_SHORT).show();

            }else{
                int index = adapter.getCurrentPosition();
                if(index > 0){
                    index -= 1;
                    adapter.getView(index, null, null).performClick();
                }else {
                    Toast.makeText(context, R.string.inicio_das_perguntas, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void trocarGrupoPergunta(boolean proxima) throws IOException {
        Log.e(TAG, "trocar grupo de perguntas");
        if(lstGrupoPerguntas != null){
            // todo @continue: trocarPergunta, precisa sinalizar o item que mudou além de configurar a variável global adequadamente
            if(adapterGrupo == null )
                adapterGrupo = (ListaGrupoPerguntasAdapter) lstGrupoPerguntas.getAdapter();

            if(proxima){
                int index = adapterGrupo.getCurrentPosition();
                if(index < adapterGrupo.getCount()-1){ // aqui fazemos menos 1 pois getCount começa no 0 e o incrementador não
                    index += 1;
                    adapterGrupo.getView(index, null, null).performClick();
//                    lstGrupoPerguntas.setSelection(index);
//                    adapterGrupo.getView(index, null, null).requestFocus();
//                    adapterGrupo.getView(index, null, null).invalidate();

                }else
                    Toast.makeText(context, R.string.ultima_pergunta_respondida, Toast.LENGTH_SHORT).show();

            }else{
                int index = adapterGrupo.getCurrentPosition();
                if(index > 0){
                    index -= 1;
                    adapterGrupo.getView(index, null, null).performClick();
                }else {
                    Toast.makeText(context, R.string.inicio_das_perguntas, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

}
