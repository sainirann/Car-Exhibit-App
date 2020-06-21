package com.example.cardetailsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Car category fragment which renders car selection
 */
public class CarCategory extends Fragment {
    private static final String MAKE_URL = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static final String MODEL_MAKES_TEMPLATE =
        "https://thawing-beach-68207.herokuapp.com/carmodelmakes/%s";
    private static final String CARS_IN_92603_TEMPLATE = "https://thawing-beach-68207.herokuapp.com/cars/%s/%s/92603";

    private List<CarMake> makeOfVehicle;
    private List<CarModel> modelOfCar;
    private List<CarBasedOnMakeAndModel> makeAndModels;
    private RecyclerView recyclerView;
    private Spinner carMakeSpinner;
    private Spinner carModelSpinner;
    private View view;

    private Map<String, String> urlToResponse = new ConcurrentHashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_car_category, container, false);

        carMakeSpinner = view.findViewById(R.id.spinner_make);
        carModelSpinner = view.findViewById(R.id.spinner_model);

        makeOfVehicle = new ArrayList<>(Collections.singletonList(CarMake.EMPTY));
        modelOfCar = new ArrayList<>(Collections.singletonList(CarModel.EMPTY));
        makeAndModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.vehicle_based_on_make_model);

        // Fetch Make from the URL and render the spinner
        try {
            parseMakeUrl(MAKE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Spinner item selection listener for make selection
        carMakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //On item(Make) selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    return;
                }
                //Hide the car information fragment(in the tablet landscape view)
                //as we are re selecting the options
                getAndHideFragmentIfNeeded();

                //Fetch all makes for this particular model
                String url = String.format(MODEL_MAKES_TEMPLATE, makeOfVehicle.get(position).getId());
                try {
                    parseModelUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner item selection listener for model selection
        carModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //On item(Model) selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    return;
                }
                CarModel carModel = modelOfCar.get(position);
                //Fetch all cars in this zip for the selected make and model
                String url = String.format(CARS_IN_92603_TEMPLATE, carModel.getVehicleMakeId(), carModel.getId());
                try {
                    parseMakeModelUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Get and Hide the car information fragment(in the tablet landscape view)
                //as we are re selecting the options
                Fragment f = getAndHideFragmentIfNeeded();

                //Make a factory to create on click listener for each recycler view item position
                //if fragment is not null, then it means tablet, if it is null then phone
                CarDetailClickListenerFactory factory = (f != null && f.isInLayout()) ?
                    new CarDetailClickListenerFactory.FragmentListenerFactory(getFragmentManager(), getContext())
                    : new CarDetailClickListenerFactory.ActivityListenerFactory(getFragmentManager(), getContext());

                //No Cars for make and model, toast that information
                if (makeAndModels.isEmpty()) {
                    Toast.makeText(getContext(), "No cars on this category", Toast.LENGTH_LONG).show();
                }

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(
                    getContext(),
                    makeAndModels,
                    factory
                );
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    /**
     * Get and hide fragment as needed. The fragment will be available only on tablet landscape view.
     * @return Fragment if it was present and in the layout.
     */
    private Fragment getAndHideFragmentIfNeeded() {
        //Check whether Car details frament is present (detect larger screen size)
        Fragment f = getFragmentManager().findFragmentById(R.id.car_details_fragment);
        //Hide As directed
        if (f != null && f.isInLayout()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(f);
            ft.commit();
        }
        return f;
    }

    /**
     * A Request task which executes an http request and read response.
     */
    class RequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // If http status is successful
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    return responseStrBuilder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    /**
     * Reads the response from the http request (responses are cached in a map for future use)
     * @param urlString Url to execute the http request
     * @return Response string
     * @throws Exception Exception when response cannot be read
     */
    private String readResponse(String urlString) throws Exception {
        String output = urlToResponse.computeIfAbsent(urlString, k -> {
            try {
                return new RequestTask().execute(urlString).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        });
        // If output is not read or empty remove it from cache.
        if (output == null || output.equals("")) {
            urlToResponse.remove(urlString);
            output = null;
        }
        return output;
    }

    /**
     * Execute the http request to extract Car Makes
     * @param urlString Url to get car makes from
     * @throws Exception Exception fetching the makes
     */
    void parseMakeUrl(String urlString) throws Exception {
        makeOfVehicle.clear();
        makeOfVehicle.add(CarMake.EMPTY);

        modelOfCar.clear();
        modelOfCar.add(CarModel.EMPTY);

        makeAndModels.clear();
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        String output = readResponse(urlString);
        if (output != null) {
            JSONArray jsonObject = new JSONArray(output);
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject car = jsonObject.getJSONObject(i);
                makeOfVehicle.add(new CarMake(car.getInt("id"), car.getString("vehicle_make")));
            }
            carMakeSpinner.setAdapter(
                    new ArrayAdapter<>(getActivity(), R.layout.make_model_spinner_dropdown_display, makeOfVehicle)
            );
        }
    }

    /**
     * Execute the http request to extract Car Model
     * @param urlString Url to get car Model from
     * @throws Exception Exception fetching the Model
     */
    void parseModelUrl(String urlString) throws Exception {
        modelOfCar.clear();
        modelOfCar.add(CarModel.EMPTY);

        makeAndModels.clear();
        String output = readResponse(urlString);
        if (output != null) {
            JSONArray jsonObject = new JSONArray(output);
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject car = jsonObject.getJSONObject(i);
                modelOfCar.add(new CarModel(car.getInt("id"), car.getString("model"), car.getInt("vehicle_make_id")));
            }
            carModelSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.make_model_spinner_dropdown_display, modelOfCar));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Execute the http request to extract Cars for the Model and make
     * @param urlString Url to get cars from
     * @throws Exception Exception fetching the cards
     */
    void parseMakeModelUrl(String urlString) throws Exception {
        makeAndModels.clear();
        String output = readResponse(urlString);
        if (output != null) {
            JSONObject jsonObject = new JSONObject(output);
            JSONArray jsonArray = jsonObject.getJSONArray("lists");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject car = jsonArray.getJSONObject(i);
                CarBasedOnMakeAndModel m = new CarBasedOnMakeAndModel(car.getString("color"),
                    car.getString("created_at"),
                    car.getInt("id"),
                    car.getString("image_url"),
                    car.getInt("mileage"),
                    car.getString("model"),
                    car.getInt("price"),
                    car.getString("veh_description"),
                    car.getString("vehicle_make"),
                    car.getString("vehicle_url"),
                    car.getString("vin_number")
                );
                makeAndModels.add(m);
            }
        }
    }
}
