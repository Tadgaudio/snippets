package com.alternativasoftware.ibs.view.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.alternativasoftware.ibs.controller.Global;
import com.alternativasoftware.ibs.controller.formulario.GravarDados;
import com.alternativasoftware.ibs.model.classes.ENT_Resposta;
import com.alternativasoftware.ibs.util.AppUtils;

/**
 * Essa classe recebeu uma leve alteração que vai receber o objeto global da aplicação e quando o
 * trabalho de receber a informação da hora terminar ela vai preencher a variável resposta atual
 * e fila da lista de respostas.
 *
 * Alterado por Tadeu Luis Pires Gaudio 16/09/2013
 */
public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

    private final ENT_Resposta resp;
    String data, dia, mes, ano;
	EditText editText;
	String titulo;
    private List<ENT_Resposta> respostasRespondidas;

    public DatePickerFragment(EditText editText, String titulo, ENT_Resposta resp) {
		this.editText = editText;
		this.titulo = titulo;
        this.resp = resp;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		
		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
		dpd.setTitle(titulo);
		return dpd;
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		
		if (day < 10) {
			dia = "0" + day;
		} else {
			dia = String.valueOf(day);
		}

		if ((month + 1) < 10) {
			mes = "0" + String.valueOf((month + 1));
		} else {
			mes = String.valueOf((month + 1));
		}

		ano = String.valueOf(year);

		data = dia + "/" + mes + "/" + ano;

        // No final da ação também adicionamos o resultado no objeto respostasRespondidas.
        Global globalVars = (Global) getActivity().getApplicationContext();

        respostasRespondidas = globalVars.getRespostasDaLista();

        if(respostasRespondidas == null)
            respostasRespondidas = new ArrayList<ENT_Resposta>();

        respostasRespondidas.add(resp);
        resp.set_Valor(data);
        editText.setText(data);

        globalVars.setRespostaAtual(resp);
        globalVars.setRespostasDaLista(respostasRespondidas);

        GravarDados.salvarResposta(getActivity(), getActivity().getApplicationContext(), resp.get_PerguntaId(), resp);

    }
}