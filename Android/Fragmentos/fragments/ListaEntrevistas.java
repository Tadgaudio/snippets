package com.alternativasoftware.ibs.view.fragments;

import com.alternativasoftware.ibs.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.alternativasoftware.ibs.controller.adapters.EstadosAdapter;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.COM_Estado;
import com.alternativasoftware.ibs.model.classes.ENT_Entrevista;
import com.alternativasoftware.ibs.controller.adapters.EntrevistasAdapter;
import com.alternativasoftware.ibs.model.dao.COM_EstadoDao;
import com.alternativasoftware.ibs.model.dao.ENT_EntrevistaDao;


/**
 * Classe Lista de Entrevistas, é um fragmento extendido de ListView e tem o objetivo separar os códigos
 * para serem trabalhados dentro do fragmento e não da Activity, dessa forma os códigos ficam mais limpos
 * e de mais simples manutenção.
 *
 * @see com.alternativasoftware.ibs.controller.adapters.EntrevistasAdapter
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListaEntrevistas extends ListFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ListaEntrevistas";
    private List<ENT_Entrevista> lista_entrevistas = new ArrayList<ENT_Entrevista>();
    private EntrevistasAdapter adapter;
    private Context context;
    private List<COM_Estado> estados;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DBAdapter db = new DBAdapter(getActivity().getApplicationContext());
        db.open();
            lista_entrevistas = ENT_EntrevistaDao.getEntrevistas();
        db.close();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new EntrevistasAdapter(getActivity(), lista_entrevistas);
        setListAdapter(adapter);

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerListaEntrevistas);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.opcoes_de_listagem, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_entrevistas, container, false);

        return view;

    }

    /**
     * Observar que esse método é o que filtra a lista.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if(adapter != null)
           // sort(adapter, 0);
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
    // TODO @complete: precisa trabalhar mais nos méotodo de filtro, para poder apresentar os itens corretos.
    public void sort(BaseAdapter adp, int tipo){
//        if(adapter != null)
//            adapter.sort(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

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