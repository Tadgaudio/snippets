package com.alternativasoftware.ibs.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alternativasoftware.ibs.R;
import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.ENT_Entrevista;
import com.alternativasoftware.ibs.controller.adapters.ListaGrupoPerguntasAdapter;
import com.alternativasoftware.ibs.model.classes.ENT_Formulario;
import com.alternativasoftware.ibs.model.classes.ENT_GrupoPergunta;
import com.alternativasoftware.ibs.model.classes.ENT_Pergunta;
import com.alternativasoftware.ibs.model.classes.ENT_TipoResposta;
import com.alternativasoftware.ibs.model.classes.ENT_TipoValor;
import com.alternativasoftware.ibs.model.classes.Global.GrupoPerguntasInfo;
import com.alternativasoftware.ibs.model.dao.ENT_EntrevistaDao;
import com.alternativasoftware.ibs.model.dao.ENT_FormularioGrupoPerguntaDao;
import com.alternativasoftware.ibs.model.dao.ENT_GrupoPerguntaDao;
import com.alternativasoftware.ibs.model.dao.ENT_PerguntaDao;
import com.alternativasoftware.ibs.model.dao.ENT_RespostaDao;
import com.alternativasoftware.ibs.model.dao.ENT_TipoRespostaDao;
import com.alternativasoftware.ibs.model.dao.ENT_TipoValorDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tadeu.gaudio on 21/08/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListaGrupoPerguntas extends ListFragment {

    private List<ENT_GrupoPergunta> lista_grupo_perguntas = new ArrayList<ENT_GrupoPergunta>();
    private ListaGrupoPerguntasAdapter adapter;
    private TextView txtEntrevistaId;
    private ENT_Entrevista entrevista;

    private Global globalVars;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        globalVars = (Global) context;  //recebe as variáveis globais
                                        //criadas na activity pai.
        if(globalVars != null)
            entrevista = globalVars.getEntrevista();

        DBAdapter db = new DBAdapter(context);
        db.open();
            lista_grupo_perguntas = ENT_GrupoPerguntaDao.getGruposByFormularioId(entrevista.get_FormularioId());
        db.close();
       //TODO @fix: tem que colocar o trabalho de filtrar a lista em um async task, além de emitir uma aviso, pois essa tarefa custa muito pra aplicação
      configuraInformacoesDosGruopos(lista_grupo_perguntas, context);

        adapter = new ListaGrupoPerguntasAdapter(getActivity(), lista_grupo_perguntas);
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     *  Busca nos grupos de perguntas quais deles tem perguntas com fotos
     *  se tiver tem que marcar que este grupo tem e ver quantas fotos tem
     *  tem que verificar quais perguntas já foram respondidas e mostrar no indicador
     *  de quantidade de perguntas respondidas.
     *
     * @param lista_grupo_perguntas recebe a lista de grupo de perguntas.
     * @param context contexto da atividade.
     */
    public void configuraInformacoesDosGruopos(List<ENT_GrupoPergunta> lista_grupo_perguntas, Context context){

        // passa por todos grupos de perguntas
        for(int i=0; i<lista_grupo_perguntas.size();i++){

            ENT_GrupoPergunta grupo = lista_grupo_perguntas.get(i);

            List<ENT_Pergunta> perguntas;
            ENT_TipoValor tipo_valor;

            DBAdapter db = new DBAdapter(context);
            db.open();
                perguntas = ENT_PerguntaDao.getPerguntasByGrupoId(grupo.get_Id());
            db.close();

            GrupoPerguntasInfo info_grupo = new GrupoPerguntasInfo();
            List<GrupoPerguntasInfo> gruposInfo = globalVars.getInformacoes_grupo_pergunta();

            info_grupo.setGrupod_id(grupo.get_Id());
            info_grupo.setQntPerguntasObrigatorias(grupo.get_QtdePerguntasObrigatorias());
            info_grupo.setQntPerguntas(perguntas.size());

            if(gruposInfo == null) {
                gruposInfo = new ArrayList<GrupoPerguntasInfo>();
            }

            // passa por todas perguntas desse grupo
            for (int i1 = 0; i1 < perguntas.size(); i1++) {

                db = new DBAdapter(context);
                db.open();
                    int id_valor = ENT_RespostaDao.getRespostaByPerguntaId(perguntas.get(i1).get_Id()).get_TipoValorId();
                    tipo_valor = ENT_TipoValorDao.getTipoValor(id_valor);
                db.close();

                // se tiver resposta com foto marcar
                if(tipo_valor.get_Sigla().equals("FT"))
                    info_grupo.setQntFotos(info_grupo.getQntFotos()+1);

                // marca quantas perguntas obrigatorias existem
                if(perguntas.get(i1).get_IndicaObrigatoria())
                    info_grupo.setQntPerguntasObrigatorias(info_grupo.getQntPerguntasObrigatorias()+1);

            }

            gruposInfo.add(info_grupo);
            globalVars.setInformacoes_grupo_pergunta(gruposInfo);

        }//fim do laço

    }
}
