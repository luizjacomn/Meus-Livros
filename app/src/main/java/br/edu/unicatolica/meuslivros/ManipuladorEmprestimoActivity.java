package br.edu.unicatolica.meuslivros;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.edu.unicatolica.meuslivros.util.AppUtils;

/**
 * Created by Gang of Three on 15/06/2016.
 */
public class ManipuladorEmprestimoActivity extends Fragment implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {
    private static final String URL = "https://biblioteca-1335.appspot.com/api/emprestimos";

    private long dataEmMillis;

    private EditText etIdEmprestimo;
    private EditText etDescricaoLivro;
    private Button buttonSalvar;

    private TableRow tableRow;
    private TextView tvDataDevolucao;
    private TextView tvDataEmprestimo;
    private Button buttonDataDevolucao;

    private void init(View view) {
        tableRow = (TableRow) view.findViewById(R.id.tableRow);
        etIdEmprestimo = (EditText) view.findViewById(R.id.etIdEmprestimo);
        etDescricaoLivro = (EditText) view.findViewById(R.id.etDescricaoLivro);

        tvDataDevolucao = (TextView) view.findViewById(R.id.tvDataDevolucao);
        tvDataEmprestimo = (TextView) view.findViewById(R.id.tvDataEmprestimo);

        buttonDataDevolucao = (Button) view.findViewById(R.id.buttonDevolucao);
        buttonSalvar = (Button) view.findViewById(R.id.buttonSalvar);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.activity_manipulador_emprestimo, container, false);

        init(view);

        buttonDataDevolucao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarData(view);
            }
        });

        buttonSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                    new HttpAsyncPOST().execute();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainContainer, new ListarEmprestimoActivity());
                    fragmentTransaction.commit();

            }
        });

        return view;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulador_emprestimo);
        init();

        buttonSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idPessoa == null || idPessoa.isEmpty()) {
                    new HttpAsyncPOST().execute();
                    Intent intent = new Intent(ManipuladorEmprestimoActivity.this, ListarEmprestimoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }*/

    private StringBuilder inputStreamToString(InputStream is) {
        String linha = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((linha = rd.readLine()) != null) {
                total.append(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    private class HttpAsyncPOST extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return post();
        }

        @Override
        protected void onPostExecute(String result) {
            //AppUtils.mensagemToast(, "Empréstimo salvo com sucesso!");
        }

    }

    private String post() {
        String mensagem = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        try {
            JSONObject json = new JSONObject();
            json.put("emprestimoID", getIdFormatado());
            json.put("descricaoLivro", etDescricaoLivro.getText().toString());
            json.put("usuarioID", HomeFragment.getProfile().getId());
            json.put("dataEmprestimo", Calendar.getInstance().getTimeInMillis());
            json.put("dataDevolucao", dataEmMillis);

            post.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(post);
            mensagem = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mensagem;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_listar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                return true;
            case R.id.listar:
                listar();
                return true;
            case R.id.sair_listar:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new LoginFragment());
        fragmentTransaction.commit();

        HomeFragment.setProfile(null);
    }

    private void listar() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new ListarEmprestimoActivity());
        fragmentTransaction.commit();
    }

    private String getIdFormatado() {
        String retorno = "";
        int numero = Integer.parseInt(etIdEmprestimo.getText().toString());
        if (numero < 10) {
            retorno = "0";
        }
        return retorno.concat(String.valueOf(numero));
    }

    // DATE PICKER
    private int year, month, day;

    public void selecionarData(View view) {
        initDate();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMax.set(cMax.get(Calendar.YEAR), 11, 31);
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

        while (cAux.getTimeInMillis() <= cMax.getTimeInMillis()) {
            if (cAux.get(Calendar.DAY_OF_WEEK) != 1 && cAux.get(Calendar.DAY_OF_WEEK) != 7) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(cAux.getTimeInMillis());

                daysList.add(c);
            }
            cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }
        daysArray = new Calendar[daysList.size()];
        for (int i = 0; i < daysArray.length; i++) {
            daysArray[i] = daysList.get(i);
        }

        datePickerDialog.setSelectableDays(daysArray);
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int ano, int mes, int dia) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day);

        year = ano;
        month = mes;
        day = dia;

        dataEmMillis = tDefault.getTimeInMillis();

        Calendar hoje = Calendar.getInstance();
        tvDataEmprestimo.setText(AppUtils.formatarData(hoje.get(Calendar.DAY_OF_MONTH), hoje.get(Calendar.MONTH), hoje.get(Calendar.YEAR)));

        tvDataDevolucao.setText(AppUtils.formatarData(day, month, year));
        tableRow.setVisibility(View.VISIBLE);
    }

    private Long getDataEmMillis() {
        return dataEmMillis;
    }

    private void initDate() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }
    }
}