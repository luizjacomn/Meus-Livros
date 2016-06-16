package br.edu.unicatolica.meuslivros.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.edu.unicatolica.meuslivros.R;
import br.edu.unicatolica.meuslivros.entity.Emprestimo;
import br.edu.unicatolica.meuslivros.util.AppUtils;

/**
 * Created by Gang of Three on 15/06/2016.
 */
public class EmprestimoListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Emprestimo> listEmprestimo;

    public EmprestimoListViewAdapter(Context context, List<Emprestimo> listEmprestimo) {
        this.listEmprestimo = listEmprestimo;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listEmprestimo.size();
    }

    @Override
    public Object getItem(int position) {
        return listEmprestimo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listEmprestimo.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_emprestimo, null);
        }
        TextView textViewIdEmprestimo = (TextView) convertView.findViewById(R.id.textViewIdEmprestimo);
        TextView textViewDescricaoLivro = (TextView) convertView.findViewById(R.id.textViewDescricaoLivro);
        //TextView textViewDataDevolucao = (TextView) convertView.findViewById(R.id.textViewDataDevolucao);

        Emprestimo emprestimo = listEmprestimo.get(position);

        textViewIdEmprestimo.setText(emprestimo.getId().toString());
        textViewDescricaoLivro.setText(emprestimo.getDescricaoLivro());
        //textViewDataDevolucao.setText(getDataDevolucaoFormatada(emprestimo));
        return convertView;
    }

    private String getDataDevolucaoFormatada(Emprestimo emprestimo) {
        Calendar calendar = emprestimo.getDataDevolucao();

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        return AppUtils.formatarData(dia, mes, ano);
    }
}