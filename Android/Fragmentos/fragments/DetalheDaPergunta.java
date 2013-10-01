package com.alternativasoftware.ibs.view.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.alternativasoftware.ibs.R;
import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.controller.adapters.ListaRespostasAdapter;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.ENT_Entrevista;
import com.alternativasoftware.ibs.model.classes.ENT_Pergunta;
import com.alternativasoftware.ibs.model.classes.ENT_Resposta;
import com.alternativasoftware.ibs.model.dao.ENT_PerguntaDao;
import com.alternativasoftware.ibs.model.dao.ENT_RespostaDao;
import com.alternativasoftware.ibs.model.dao.ENT_TipoRespostaDao;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe DetalheDapergunta extende um fragmento do tipo list(ListFragment), elá foi desenvolvida com
 * a intenção de separar os códigos melhor, com o uso de fragmentos podemos manipular os eventos da lista
 * fora da active em execução.
 *
 * @see com.alternativasoftware.ibs.view.fragments.DetalhesGrupoPerguntas
 * Created by tadeu.gaudio on 20/08/13.
 */
public class DetalheDaPergunta extends ListFragment implements AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "DetalheDaPergunta";
    private List<ENT_Resposta> lista_respostas = new ArrayList<ENT_Resposta>();
    private ListaRespostasAdapter adapter;
    private int TAKE_PHOTO_CODE = 1024;
    private int pergunta_id;
    private Activity activity;
    private Button proxima;
    private Button anterior;
    private String siglaTipoResp;
    private Global globalVars;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        DBAdapter db = new DBAdapter(activity.getApplicationContext());
        db.open();

            siglaTipoResp = ENT_TipoRespostaDao.getTipoResposta(
                    ENT_PerguntaDao.getPergunta(pergunta_id).get_TipoRespostaId()
                ).get_Sigla();

            lista_respostas = ENT_RespostaDao.getRespostasByPerguntaId(pergunta_id);

        db.close();

        adapter = new ListaRespostasAdapter(getActivity(), lista_respostas, siglaTipoResp);
        setListAdapter(adapter);
        getListView().setSelection(getSelectedItemPosition());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_respostas, container, false);

        globalVars = (Global) getActivity().getApplicationContext();
        DBAdapter db = new DBAdapter(getActivity().getApplicationContext());
        db.open();
            globalVars.setPerguntaSelecionada(
                ENT_PerguntaDao.getPergunta(pergunta_id)
            );
        db.close();

       return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//        if(adapter != null)
        // sort(adapter, 0);

    }

    public void setArguments(int pergunta_id){
        this.pergunta_id = pergunta_id;
    }


    /**
     * Faz um sort na lista com de acordo com o tipo de código
     * passado para o parametro tipo.
     * <p>
     *
     * @param adp adaptador do tipo EntrevistaAdapter
     * @param tipo inteiro representando um dos estados abaixo:
     *          MOSTRAR_TODAS       = 0
     *          APENAS_ENVIADAS     = 1
     *          APENAS_NAO_ENVIADAS = 2
     *          APENAS_ATUALIZADAS  = 3
     *          APENAS_COM_ERRO     = 4
     */
    public void sort(BaseAdapter adp, int tipo){
//        if(adapter != null)
//            adapter.sort(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.imageDelete:
                Toast.makeText(getActivity(), "Vamos apagar esse imagem papito!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

//    @Override
//    public void executar() throws Exception{
//        this.entrevistas = EntrevistaService.getEntrevistas(getActivity(), consultorId);
//    }
//
//
//    @Override
//    public void atualizarView(){
//        //Atualiza a lista de entrevistas na thread principal.
//        if(entrevistas != null && !entrevistas.isEmpty() && getActivity() != null)
//            listview.setAdapter(new EntrevistasAdapter(getActivity(), listview, entrevistas));
//    }




}