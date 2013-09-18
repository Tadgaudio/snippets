package com.alternativasoftware.ibs.controller.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alternativasoftware.ibs.R;
import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.model.DBAdapter;
import com.alternativasoftware.ibs.model.classes.ENT_Entrevista;
import com.alternativasoftware.ibs.model.classes.ENT_Formulario;
import com.alternativasoftware.ibs.model.classes.ENT_Pergunta;
import com.alternativasoftware.ibs.model.classes.ENT_TipoResposta;
import com.alternativasoftware.ibs.model.dao.ENT_FormularioDao;
import com.alternativasoftware.ibs.model.dao.ENT_TipoRespostaDao;
import com.alternativasoftware.ibs.view.fragments.DetalheDaPergunta;

import java.util.List;

/**
 * Created by tadeu.gaudio on 26/08/13.
   Modificado em 18/09/2013, este arquivo serve de exemplo completo de um adaptador para o listView, com 
   suporte a destaque de itens quando clicado.

 */
public class ListaPerguntasAdapter extends BaseAdapter {
    private static final String TAG = "ListaPerguntasAdapter";
    private Activity context;
    private List<ENT_Pergunta>  lista;
    private boolean[] arrBgcolor;
    private int currentPosition = 0;

    private View currentSelectedView;
    private Boolean firstTimeStartup = true;


    public ListaPerguntasAdapter(Activity context, List<ENT_Pergunta> lista) {
        super();
        this.context = context;
        this.lista = lista;
        this.arrBgcolor = new boolean[lista.size()];

    }

    @Override
    public int getCount() {
        return lista != null ? lista.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return lista != null ? lista.get(i): null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public ENT_Pergunta getPerguntaAtual(){
        return lista.get(currentPosition);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder vh = new ViewHolder();
        final int i2 = i;
        if(v == null){
            LayoutInflater vi = context.getLayoutInflater();
            v = vi.inflate(R.layout.item_lista_perguntas, null);

            vh.tituloPergunta = (TextView) v.findViewById(R.id.tituloPergunta);
            vh.iconeTemFoto = (ImageButton) v.findViewById(R.id.iconeTemFoto);
            vh.iconeObrigatoria = (ImageButton) v.findViewById(R.id.iconeObrigatoria);
            vh.iconeRespondida = (ImageButton) v.findViewById(R.id.iconeRespondida);

            v.setTag(vh);
        }else {
            vh = (ViewHolder )v.getTag();
        }


        /**
         * Seta o click no item da lista e ativa o painel
         * de informações envia o id do produtor e da propriedade.
         */
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewHolder vh = new ViewHolder();
                vh = (ViewHolder) v.getTag();

                currentPosition = i2;
                Log.e(TAG, "Item clicado é: "+String.valueOf(i2));

                if (firstTimeStartup) {// first time  highlight first row
                    currentSelectedView = v;
                }

                firstTimeStartup = false;
                if (currentSelectedView != null && currentSelectedView != v) {
                    unhighlightCurrentRow(currentSelectedView);
                }

                currentSelectedView = v;
                highlightCurrentRow(currentSelectedView);


                abrirListaRespostas(context, vh, vh.pergunta_id);

            }
        });

        ENT_Pergunta obj = lista.get(i);
        // todo @complete: tem que verificar quais perguntas já foram respondidas e sinalizar isso
        // todo @fix: verificar porquê os objetos de foto as vezes somem em certos grupos.\
        try{
            vh.tituloPergunta.setText(obj.get_Nome());
            vh.pergunta_id = obj.get_Id();

            //esconde icone de foto caso a pergunta não seja do tipo foto.
            DBAdapter db = new DBAdapter(context);
            db.open();
                ENT_TipoResposta tipoResposta = ENT_TipoRespostaDao.getTipoResposta(obj.get_TipoRespostaId());
            db.close();
            if(!tipoResposta.get_Sigla().equals("FT"))
                vh.iconeTemFoto.setVisibility(View.GONE);

            //esconde o icone de obrigatória caso não seja uma pergunta obrigatória.
            if(!obj.get_IndicaObrigatoria())
                vh.iconeObrigatoria.setVisibility(View.GONE);


        }catch (Exception e){
            Log.e(TAG, "===========================================================");
            Log.e(TAG, e.getMessage(), e);
            Log.e(TAG, "===========================================================");
        }

        return v;
    }

    class ViewHolder{
        TextView tituloPergunta;
        ImageButton iconeTemFoto;
        ImageButton iconeObrigatoria;
        ImageButton iconeRespondida;
        int pergunta_id;
    }


    /**
     * ----------------------------------- Métodos especiais. -------------------------------
     */

    /**
     * Essa função tem o objetivo de desmarcar os itens da lista que não estão selecionados.
     * @param rowView recebe a view do item selecionado na ListView
     */
    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackground(null);
    }


    /**
     * Essa função tem o objetivo de marcar os item que foi selecionado atualmente na lista.
     * @param rowView recebe a view do item selecionado na ListView
     */
    private void highlightCurrentRow(View rowView) {
        rowView.setBackgroundResource(R.color.azul_barra_superior);
    }

    /**
     * Abre o fragmento de detalhes da entrevista com os dados da propriedade e do produtor.
     * <p>
     * Testa também para saber se o painel já não está aberto.
     *
     * @param context contexto aqui é do tipo Acitivity ao invés de Context
     *                pois, a lista está trabalhando dentro de um fragment.
     * @param vh ViewHolder para poder buscar informações do
     *           item clicado
     * @param perguntaId Inteiro com o id da pergunta que vai
     *                     ser passado para o método de construtor.
     *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void abrirListaRespostas(Activity context, ViewHolder vh, int perguntaId){

        DetalheDaPergunta detailsPergunta = new DetalheDaPergunta();

        FragmentManager fm = context.getFragmentManager();
        detailsPergunta.setArguments(perguntaId);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            context.findViewById(R.id.imgBanner).setBackgroundDrawable(null);
        } else {
            context.findViewById(R.id.imgBanner).setBackground(null);
        }

        destroyGlobalVarsByPergunta();
        
        fm.beginTransaction().replace(R.id.painelInfoPergunta, detailsPergunta).commit();
    }

    private void destroyGlobalVarsByPergunta() {
        Global globalVars = (Global) context.getApplicationContext();

        globalVars.setRespostaAtual(null);
        globalVars.setRespostasMultiplaescolha(null);

    }

}
