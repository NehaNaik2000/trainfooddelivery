package my.trainfooddelivery.app.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import my.trainfooddelivery.app.R;
import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.material.textfield.TextInputLayout;

public class customertrainfragment extends Fragment {
    Button search;
    EditText trainno;
    TextView res;
    String t;
   // private Spinner resultSpinner;
    boolean userSelected=false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_customertrainfragment, null);
        getActivity().setTitle("Place Order");


       Spinner resultSpinner = v.findViewById(R.id.result_spinner);

        trainno = v.findViewById(R.id.Trainno);
        search = v.findViewById(R.id.search);
        // res=findViewById(R.id.result);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t = trainno.getText().toString();
                //String startDay = "0";
                try {
                    fetchData(t,resultSpinner);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;

    }

    private void fetchData(String trainno,Spinner resultSpinner) throws IOException {
        String url = "https://irctc1.p.rapidapi.com/api/v1/liveTrainStatus?trainNo=" + trainno; //"&startDay=" + startDay;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "14f37fd866mshdb34ab5060af2bfp101673jsn5d79e67274e8")
                .addHeader("X-RapidAPI-Host", "irctc1.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Some error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(resp);
                                JSONArray upcoming_stations = json.getJSONObject("data").getJSONArray("upcoming_stations");
                                List<String> stationNames = new ArrayList<>();
                                TreeMap<Integer, String> stationMap = new TreeMap<>();
                                for (int i = 0; i < upcoming_stations.length(); i++) {
                                    JSONObject station = upcoming_stations.getJSONObject(i);
                                    String stationName = station.getString("station_name");
                                    if (stationName == null || stationName.isEmpty()) {
                                        // Handle the error, such as displaying an error message or skipping the station
                                        continue;
                                    }
                                    String stateCode = station.getString("state_code");

                                    String eta =station.getString("eta");
                                    // Assuming that the ETA is stored in a String variable named "eta" in the format "HH:mm"
                                    String[] parts = eta.split(":"); // Split the ETA into hours and minutes
                                    int hours = Integer.parseInt(parts[0]); // Parse the hours as an integer
                                    int minutes = Integer.parseInt(parts[1]); // Parse the minutes as an integer
                                    int etaMinutes = hours * 60 + minutes; // Convert the hours to minutes and add the minutes
                                    int currentMinutes = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60
                                            + Calendar.getInstance().get(Calendar.MINUTE); // Get the current time in minutes
                                    int minutesUntilArrival = etaMinutes - currentMinutes; // Calculate the minutes until arrival

                                       if(minutesUntilArrival>60) {
//                                           String stationFullName = stationName + ", " + stateCode + ", eta: " + minutesUntilArrival +", min"+", time: "+eta;
                                           String stationFullName = stationName + ", " + stateCode + ", eta: " + minutesUntilArrival + "min" + ", time: " + eta;
                                           stationMap.put(minutesUntilArrival, stationFullName);
                                       }
                                }
                                ArrayList<String> sortedStationNames = new ArrayList<>();
                                for (Map.Entry<Integer, String> entry : stationMap.entrySet()) {
                                    sortedStationNames.add(entry.getValue());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android. R.layout.simple_spinner_dropdown_item, sortedStationNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                resultSpinner.setAdapter(adapter);



                                Log.d("Spinner", "Adapter count: " + adapter.getCount());
                               Log.d("Spinner", "Spinner parent: " + resultSpinner.getDisplay());


//resultSpinner.setSelected(false);
//resultSpinner.setSelection(0,true);
                                resultSpinner.setSelection(adapter.NO_SELECTION,true);
                                resultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            String selectedStationFullName = sortedStationNames.get(position);
                                            String[] parts = selectedStationFullName.split(",\\s*");
                                            String selectedStationName = parts[0];
                                            String selectedStateCode = parts[1];
                                            String etamin=parts[2].substring(5);
                                            String eta=parts[3].substring(6);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("selectedStationName", selectedStationName);
                                            bundle.putString("selectedStateCode", selectedStateCode);
                                            bundle.putString("eta",etamin);
                                            bundle.putString("etatime",eta);
                                            bundle.putString("trainno",trainno);
                                            Log.d("bundle","bundle"+bundle.getString("selectedStationName"));
                                            CustomerHomeFragment fragment = new CustomerHomeFragment();
                                            fragment.setArguments(bundle);
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();



                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        Log.d("error","error");
                                        // Do nothing
                                    }
                                });

// Set the flag to true when the user manually selects an item
                                resultSpinner.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        userSelected = true;
                                        return false;
                                    }
                                });





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}