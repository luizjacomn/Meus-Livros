package br.edu.unicatolica.meuslivros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.login.LoginManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.unicatolica.meuslivros.adapter.EmprestimoListViewAdapter;
import br.edu.unicatolica.meuslivros.entity.Emprestimo;

/**
 * Created by Gang of Three on 15/06/2016.
 */
public class ListarEmprestimoActivity extends Fragment {

    private static String URL = "https://biblioteca-1335.appspot.com/api/emprestimos/user/";

    private ListView listViewEmprestimos;

    /*private void init() {
        listViewEmprestimos = (ListView) findViewById(R.id.listViewEmprestimos);

        new HttpAsyncTask().execute();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        View view = inflater.inflate(R.layout.activity_listar_emprestimo, container, false);
        listViewEmprestimos = (ListView) view.findViewById(R.id.listViewEmprestimos);

        new HttpAsyncTask().execute();

        return view;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_emprestimo);
        init();
    }*/

    private String getAll() {
        String resposta = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet get = new HttpGet(URL + HomeFragment.getProfile().getId());
        try {

            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(get);
            resposta = inputStreamToString(response.getEntity().getContent()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return getAll();
        }

        @Override
        protected void onPostExecute(String result) {
            List<Emprestimo> listEmprestimo = new ArrayList<Emprestimo>();
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setId(jsonObject.getLong("emprestimoID"));
                    emprestimo.setDescricaoLivro(jsonObject.getString("descricaoLivro"));

                    //Calendar dataDevolucao = getDataEmMillis(jsonObject.getLong("dataDevolucao"));
                    //emprestimo.setDataDevolucao(dataDevolucao);
                    listEmprestimo.add(emprestimo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            EmprestimoListViewAdapter adapter = new EmprestimoListViewAdapter(getActivity().getBaseContext(), listEmprestimo);
            listViewEmprestimos.setAdapter(adapter);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_cadastrar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.sair_cadastro:
                logout();
                return true;
            case R.id.cadastrar:
                cadastrarEmprestimo();
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

    private void cadastrarEmprestimo() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new ManipuladorEmprestimoActivity());
        fragmentTransaction.commit();
    }

    private Calendar getDataEmMillis(long dataEmMillis) {
        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(dataEmMillis);
        return data;
    }

}